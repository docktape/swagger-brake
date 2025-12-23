package com.docktape.swagger.brake.core.model.transformer;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersion;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersionContext;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Resolves schema types in a version-aware manner, handling both OpenAPI 3.0.x
 * single type strings and OpenAPI 3.1.x type arrays.
 * 
 * <p>OpenAPI 3.0.x uses a single {@code type} field (e.g., "string") and a separate
 * {@code nullable} flag. OpenAPI 3.1.x uses JSON Schema's type arrays
 * (e.g., ["string", "null"]) to express nullable types.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TypeResolver {
    private static final String DEFAULT_TYPE = "object";
    private static final String NULL_TYPE = "null";
    
    private final CheckerOptionsProvider checkerOptionsProvider;
    
    /**
     * Resolves the primary type from a schema, handling both OpenAPI 3.0.x
     * single type strings and OpenAPI 3.1.x type arrays.
     * 
     * <p>For OpenAPI 3.1.x specs with type arrays:</p>
     * <ul>
     *   <li>Filters out "null" from the array to find the primary type</li>
     *   <li>Returns the first non-null type if multiple types exist</li>
     *   <li>Logs a warning if multiple non-null types are found (union types)</li>
     *   <li>Returns "object" as default if only "null" or empty array</li>
     * </ul>
     * 
     * <p>For OpenAPI 3.0.x specs:</p>
     * <ul>
     *   <li>Returns the single {@code type} field value</li>
     *   <li>Returns "object" as default if type is null</li>
     * </ul>
     * 
     * @param schema the swagger-parser Schema object
     * @return the primary type string (never null)
     */
    public String resolveType(Schema<?> schema) {
        if (schema == null) {
            log.debug("Schema is null, returning default type: {}", DEFAULT_TYPE);
            return DEFAULT_TYPE;
        }
        
        OpenApiVersion version = OpenApiVersionContext.getCurrentVersion();
        
        // OpenAPI 3.1.x: Check for type array
        if (version != null && version.is3_1()) {
            Set<String> types = schema.getTypes();
            if (types != null && !types.isEmpty()) {
                log.trace("OpenAPI 3.1.x: Processing type array: {}", types);
                
                // Filter out "null" to find primary types
                Set<String> nonNullTypes = types.stream()
                    .filter(t -> !NULL_TYPE.equals(t))
                    .collect(Collectors.toSet());
                
                if (nonNullTypes.isEmpty()) {
                    // Only "null" type exists
                    log.debug("Type array contains only 'null', returning default type: {}", DEFAULT_TYPE);
                    return DEFAULT_TYPE;
                }
                
                if (nonNullTypes.size() > 1) {
                    // Multiple non-null types (union type)
                    log.warn("Schema has multiple non-null types (union type): {}. "
                            + "Taking first type '{}'. Full union type support is not yet implemented.",
                            nonNullTypes, nonNullTypes.iterator().next());
                }
                
                String primaryType = nonNullTypes.iterator().next();
                log.trace("Resolved primary type from array: {}", primaryType);
                return primaryType;
            }
            
            // Fall through to 3.0.x logic if types array is null/empty
            log.trace("OpenAPI 3.1.x: Type array is null or empty, falling back to getType()");
        }
        
        // OpenAPI 3.0.x or fallback: Use single type field
        String type = schema.getType();
        if (type == null) {
            handleNullType();
            return DEFAULT_TYPE;
        }
        
        log.trace("Resolved type from getType(): {}", type);
        return type;
    }
    
    /**
     * Handles null type scenarios with appropriate logging and strict mode validation.
     * In strict validation mode, throws an exception. Otherwise, logs a warning.
     */
    private void handleNullType() {
        CheckerOptions options = checkerOptionsProvider.get();
        boolean strictMode = options != null && options.isStrictValidation();
        
        String message = "Schema has null type, using default 'object' type. "
                + "This may indicate an invalid or malformed OpenAPI specification. "
                + "In Swagger 2.0 and OpenAPI 3.0.x, parameters should define 'type' directly, "
                + "not use a 'schema' property (except for body parameters).";
        
        if (strictMode) {
            throw new IllegalStateException("Schema does not have any type. " + message);
        } else {
            log.trace(message);
        }
    }
    
    /**
     * Determines if a schema is nullable based on version-specific logic.
     *
     * <p>For OpenAPI 3.1.x: checks if "null" is present in the type array.</p>
     *
     * <p>For OpenAPI 3.0.x: checks the {@code nullable} flag.</p>
     * 
     * @param schema the swagger-parser Schema object
     * @return true if the schema allows null values, false otherwise
     */
    public boolean isNullableFromTypeArray(Schema<?> schema) {
        if (schema == null) {
            return false;
        }
        
        OpenApiVersion version = OpenApiVersionContext.getCurrentVersion();
        
        // OpenAPI 3.1.x: Check if "null" is in type array
        if (version != null && version.is3_1()) {
            Set<String> types = schema.getTypes();
            if (types != null) {
                boolean isNullable = types.contains(NULL_TYPE);
                log.trace("OpenAPI 3.1.x: Schema nullable check via type array: {}", isNullable);
                return isNullable;
            }
        }
        
        // OpenAPI 3.0.x: Use nullable flag
        boolean isNullable = Boolean.TRUE.equals(schema.getNullable());
        log.trace("OpenAPI 3.0.x: Schema nullable check via nullable flag: {}", isNullable);
        return isNullable;
    }
}
