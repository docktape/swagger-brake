package com.docktape.swagger.brake.core.model.transformer;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.*;

import com.docktape.swagger.brake.core.model.Schema;
import com.docktape.swagger.brake.core.model.SchemaAttribute;
import com.docktape.swagger.brake.core.model.SchemaBuilder;
import com.docktape.swagger.brake.core.model.service.TypeRefNameResolver;
import com.docktape.swagger.brake.core.model.store.SchemaStore;
import com.docktape.swagger.brake.core.model.store.StoreProvider;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.parser.util.SchemaTypeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class SchemaTransformer implements Transformer<io.swagger.v3.oas.models.media.Schema, Schema> {
    private final TypeRefNameResolver typeRefNameResolver;

    @Override
    public Schema transform(io.swagger.v3.oas.models.media.Schema from) {
        try {
            return internalTransform(from);
        } finally {
            SeenRefHolder.clear();
        }
    }

    private Schema internalTransform(io.swagger.v3.oas.models.media.Schema swSchema) {
        if (swSchema == null) {
            return null;
        }
        if (swSchema instanceof ArraySchema) {
            Schema schema = internalTransform(((ArraySchema) swSchema).getItems());
            return new SchemaBuilder(swSchema.getType())
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
        String schemaType = swSchema.getType();
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
        schemaBuilder.exclusiveMaximum(swSchema.getExclusiveMaximum());
        schemaBuilder.exclusiveMinimum(swSchema.getExclusiveMinimum());
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
            Schema schema = internalTransform(newInternalSchema);
            String attributeName = e.getKey();
            Boolean deprecatedInSchema = newInternalSchema.getDeprecated();
            boolean deprecated = deprecatedInSchema == null ? false : deprecatedInSchema;
            boolean required = requiredAttributes.contains(attributeName);
            result.add(new SchemaAttribute(attributeName, schema, required, deprecated));
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
}
