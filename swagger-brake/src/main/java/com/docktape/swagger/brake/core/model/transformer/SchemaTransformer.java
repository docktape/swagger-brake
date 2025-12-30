package com.docktape.swagger.brake.core.model.transformer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

import org.apache.commons.collections4.CollectionUtils;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import org.springframework.stereotype.Component;

import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.Schema;
import com.docktape.swagger.brake.core.model.SchemaAttribute;
import com.docktape.swagger.brake.core.model.SchemaBuilder;
import com.docktape.swagger.brake.core.model.service.TypeRefNameResolver;
import com.docktape.swagger.brake.core.model.store.SchemaStore;
import com.docktape.swagger.brake.core.model.store.StoreProvider;
import com.docktape.swagger.brake.core.util.SafeSwaggerSerializer;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersion;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersionContext;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.parser.util.SchemaTypeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unchecked")
public class SchemaTransformer implements Transformer<io.swagger.v3.oas.models.media.Schema, Schema> {
    private final TypeRefNameResolver typeRefNameResolver;
    private final TypeResolver typeResolver;
    private final SafeSwaggerSerializer safeSwaggerSerializer;
    private final CheckerOptionsProvider checkerOptionsProvider;

    @Override
    public Schema transform(io.swagger.v3.oas.models.media.Schema from) {
        try {
            SchemaTransformationContext.initialize(checkerOptionsProvider.get().getMaxSchemaTransformationDepth());
            return internalTransform(from);
        } finally {
            SeenRefHolder.clear();
            SchemaTransformationContext.clear();
        }
    }

    private Schema internalTransform(io.swagger.v3.oas.models.media.Schema swSchema) {
        if (swSchema == null) {
            return null;
        }
        if (swSchema instanceof ArraySchema) {
            Schema schema = internalTransform(((ArraySchema) swSchema).getItems());
            return new SchemaBuilder(typeResolver.resolveType(swSchema))
                .schema(schema).schemaAttributes(getSchemaAttributes(swSchema))
                .maxItems(swSchema.getMaxItems())
                .minItems(swSchema.getMinItems())
                .uniqueItems(swSchema.getUniqueItems())
                .build();
        } else if (swSchema instanceof ComposedSchema) {
            return transformComposedSchema((ComposedSchema) swSchema);
        } else {
            return transformSchema(swSchema);
        }
    }

    private Schema transformComposedSchema(ComposedSchema swSchema) {
        List<io.swagger.v3.oas.models.media.Schema> schemas;
        if (CollectionUtils.isNotEmpty(swSchema.getAllOf())) {
            schemas = swSchema.getAllOf();
        } else if (CollectionUtils.isNotEmpty(swSchema.getOneOf())) {
            schemas = swSchema.getOneOf();
        } else if (CollectionUtils.isNotEmpty(swSchema.getAnyOf())) {
            schemas = swSchema.getAnyOf();
        } else {
            throw new IllegalStateException("Composed schema is used that is not allOf, oneOf nor anyOf.");
        }
        Collection<SchemaAttribute> objectAttributes = schemas
            .stream()
            .map(this::transformSchema)
            .map(Schema::getSchemaAttributes)
            .flatMap(Collection::stream)
            .collect(toSet());
        return new SchemaBuilder(SchemaTypeUtil.OBJECT_TYPE).schemaAttributes(objectAttributes).build();
    }

    private Schema transformSchema(io.swagger.v3.oas.models.media.Schema swSchema) {
        String ref = swSchema.get$ref();
        if (isNotBlank(ref) && SeenRefHolder.isNotSeen(ref)) {
            SeenRefHolder.store(ref);
            Schema resolvedSchema = getSchemaForRef(ref);
            SeenRefHolder.remove(ref);
            return resolvedSchema;
        }
        String schemaType = typeResolver.resolveType(swSchema);
        if (isNotBlank(ref) && SeenRefHolder.isSeen(ref) && isBlank(schemaType)) {
            return null;
        }
        if (isBlank(schemaType)) {
            // you can create a schema in JSON format without any definition, so let's fall back to object type here
            schemaType = "object";
        }
        SchemaBuilder schemaBuilder = new SchemaBuilder(schemaType);
        schemaBuilder.maxLength(swSchema.getMaxLength());
        schemaBuilder.minLength(swSchema.getMinLength());
        schemaBuilder.maxItems(swSchema.getMaxItems());
        schemaBuilder.minItems(swSchema.getMinItems());
        schemaBuilder.uniqueItems(swSchema.getUniqueItems());
        schemaBuilder.maximum(swSchema.getMaximum());
        schemaBuilder.minimum(swSchema.getMinimum());
        
        // Handle exclusive bounds based on OpenAPI version
        // In OpenAPI 3.0.x: exclusiveMaximum/exclusiveMinimum are Boolean modifiers, stored via getExclusiveMaximum()/getExclusiveMinimum()
        // In OpenAPI 3.1.x: exclusiveMaximum/exclusiveMinimum are BigDecimal values, stored via getExclusiveMaximumValue()/getExclusiveMinimumValue()
        OpenApiVersion version = OpenApiVersionContext.getCurrentVersion();
        log.trace("Transforming schema with type '{}', current version context: {}", schemaType, version);
        
        if (version != null && version.is3_1()) {
            log.trace("OpenAPI 3.1.x detected for schema transformation. {}", safeSwaggerSerializer.serialize(swSchema));
            // OpenAPI 3.1.x: Use the numeric *Value() methods for exclusive bounds
            BigDecimal exclusiveMaxValue = swSchema.getExclusiveMaximumValue();
            BigDecimal exclusiveMinValue = swSchema.getExclusiveMinimumValue();
            log.trace("OpenAPI 3.1.x schema - exclusiveMaxValue={}, exclusiveMinValue={}, max={}, min={}", 
                exclusiveMaxValue, exclusiveMinValue, swSchema.getMaximum(), swSchema.getMinimum());
            
            if (exclusiveMaxValue != null) {
                log.debug("Setting exclusive maximum value: {} (OpenAPI 3.1.x)", exclusiveMaxValue);
                schemaBuilder.exclusiveMaximumValue(exclusiveMaxValue);
                // Don't set maximum - in 3.1.x, exclusiveMaximum replaces maximum
            }
            if (exclusiveMinValue != null) {
                log.debug("Setting exclusive minimum value: {} (OpenAPI 3.1.x)", exclusiveMinValue);
                schemaBuilder.exclusiveMinimumValue(exclusiveMinValue);
                // Don't set minimum - in 3.1.x, exclusiveMinimum replaces minimum
            }
        } else {
            // OpenAPI 3.0.x: exclusiveMaximum/exclusiveMinimum are boolean modifiers
            log.trace("OpenAPI 3.0.x schema - exclusiveMax={}, exclusiveMin={}, max={}, min={}", 
                swSchema.getExclusiveMaximum(), swSchema.getExclusiveMinimum(), 
                swSchema.getMaximum(), swSchema.getMinimum());
            schemaBuilder.exclusiveMaximum(swSchema.getExclusiveMaximum());
            schemaBuilder.exclusiveMinimum(swSchema.getExclusiveMinimum());
        }
        
        schemaBuilder.schemaAttributes(getSchemaAttributes(swSchema));
        List rawEnums = swSchema.getEnum();
        if (CollectionUtils.isNotEmpty(rawEnums)) {
            List<String> enumValues = rawEnums.stream().filter(Objects::nonNull).map(Object::toString).toList();
            schemaBuilder.enumValues(enumValues);
        }
        return schemaBuilder.build();
    }

    private List<SchemaAttribute> getSchemaAttributes(io.swagger.v3.oas.models.media.Schema swSchema) {
        Map<String, io.swagger.v3.oas.models.media.Schema> properties = swSchema.getProperties();
        if (properties == null) {
            return Collections.emptyList();
        }
        Set<String> requiredAttributes = new HashSet<>();
        if (CollectionUtils.isNotEmpty(swSchema.getRequired())) {
            requiredAttributes.addAll(swSchema.getRequired());
        }

        Set<Map.Entry<String, io.swagger.v3.oas.models.media.Schema>> entries = properties.entrySet();
        List<SchemaAttribute> result = new ArrayList<>();
        for (Map.Entry<String, io.swagger.v3.oas.models.media.Schema> e : entries) {
            io.swagger.v3.oas.models.media.Schema newInternalSchema = e.getValue();
            
            // Check for circular reference or depth limit before recursing
            if (SchemaTransformationContext.isCircularReference(newInternalSchema)) {
                if (log.isTraceEnabled()) {
                    log.trace("Circular reference detected for property '{}', skipping to prevent StackOverflowError. Schema: {}",
                        e.getKey(), safeSwaggerSerializer.serialize(newInternalSchema));
                }
                continue; // Skip this property to prevent infinite recursion
            }
            
            if (SchemaTransformationContext.isDepthLimitReached()) {
                if (log.isTraceEnabled()) {
                    log.trace("Max transformation depth reached for property '{}', skipping further nesting. Schema: {}",
                        e.getKey(), safeSwaggerSerializer.serialize(newInternalSchema));
                }
                continue; // Skip this property to prevent excessive depth
            }
            
            SchemaTransformationContext.enterSchema(newInternalSchema);
            try {
                Schema schema = internalTransform(newInternalSchema);
                String attributeName = e.getKey();
                Boolean deprecatedInSchema = newInternalSchema.getDeprecated();
                boolean deprecated = deprecatedInSchema == null ? false : deprecatedInSchema;
                boolean required = requiredAttributes.contains(attributeName);
                result.add(new SchemaAttribute(attributeName, schema, required, deprecated));
            } finally {
                SchemaTransformationContext.exitSchema(newInternalSchema);
            }
        }
        return result;
    }

    private Schema getSchemaForRef(String originalRefName) {
        if (originalRefName == null) {
            return null;
        }
        String refName = typeRefNameResolver.resolve(originalRefName);
        SchemaStore schemaStore = StoreProvider.provideSchemaStore();
        if (schemaStore == null) {
            return null;
        }
        io.swagger.v3.oas.models.media.Schema nativeSchema = schemaStore.getNative(refName).orElseThrow(() -> new IllegalStateException("Reference not found for " + refName));
        Schema transformedSchema =
            schemaStore.getTransformer(refName, () -> internalTransform(nativeSchema)).orElseThrow(() -> new IllegalArgumentException("Transformed schema cannot be resolved"));
        return transformedSchema;
    }

    /*
     * The purpose of this class is to keep track of already seen schema references to avoid recursive schemas breaking the functionality.
     */
    private static class SeenRefHolder {
        private static final ThreadLocal<Collection<String>> HOLDER = new ThreadLocal<>();

        private static Collection<String> get() {
            Collection<String> seenRefs = HOLDER.get();
            if (seenRefs == null) {
                seenRefs = new HashSet<>();
                HOLDER.set(seenRefs);
            }
            return seenRefs;
        }

        static boolean isSeen(String refName) {
            return get().contains(refName);
        }

        static boolean isNotSeen(String refName) {
            return !isSeen(refName);
        }

        static void store(String refName) {
            get().add(refName);
        }

        static void remove(String refName) {
            get().remove(refName);
        }

        static void clear() {
            HOLDER.remove();
        }
    }

    /*
     * The purpose of this class is to detect circular references in inline schema property traversal
     * and enforce depth limits to prevent StackOverflowError. Unlike SeenRefHolder which tracks $ref
     * names, this tracks actual schema object instances using identity-based comparison.
     */
    private static class SchemaTransformationContext {
        private static final ThreadLocal<Context> HOLDER = new ThreadLocal<>();

        private static class Context {
            private final Set<io.swagger.v3.oas.models.media.Schema> seenSchemas;
            private final int maxDepth;
            private int currentDepth;

            Context(int maxDepth) {
                // Use IdentityHashMap to track object instances, not equality
                this.seenSchemas = Collections.newSetFromMap(new java.util.IdentityHashMap<>());
                this.maxDepth = maxDepth;
                this.currentDepth = 0;
            }
        }

        static void initialize(int maxDepth) {
            HOLDER.set(new Context(maxDepth));
        }

        static void clear() {
            HOLDER.remove();
        }

        static boolean isCircularReference(io.swagger.v3.oas.models.media.Schema schema) {
            Context context = HOLDER.get();
            if (context == null) {
                return false;
            }
            return context.seenSchemas.contains(schema);
        }

        static boolean isDepthLimitReached() {
            Context context = HOLDER.get();
            if (context == null) {
                return false;
            }
            return context.currentDepth >= context.maxDepth;
        }

        static void enterSchema(io.swagger.v3.oas.models.media.Schema schema) {
            Context context = HOLDER.get();
            if (context != null) {
                context.seenSchemas.add(schema);
                context.currentDepth++;
            }
        }

        static void exitSchema(io.swagger.v3.oas.models.media.Schema schema) {
            Context context = HOLDER.get();
            if (context != null) {
                context.seenSchemas.remove(schema);
                context.currentDepth--;
            }
        }
    }
}
