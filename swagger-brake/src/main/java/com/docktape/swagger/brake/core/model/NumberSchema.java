package com.docktape.swagger.brake.core.model;

import java.math.BigDecimal;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class NumberSchema extends Schema {
    private final BigDecimal maximum;
    private final BigDecimal minimum;
    
    /**
     * Deprecated: Use {@link #exclusiveMaximumValue} instead.
     *
     * @deprecated Use {@link #exclusiveMaximumValue} instead. This field is maintained for backward compatibility
     *     with OpenAPI 3.0.x where exclusiveMaximum was a boolean modifier.
     */
    @Deprecated
    private final boolean exclusiveMaximum;
    
    /**
     * Deprecated: Use {@link #exclusiveMinimumValue} instead.
     *
     * @deprecated Use {@link #exclusiveMinimumValue} instead. This field is maintained for backward compatibility
     *     with OpenAPI 3.0.x where exclusiveMinimum was a boolean modifier.
     */
    @Deprecated
    private final boolean exclusiveMinimum;
    
    /**
     * The exclusive maximum value constraint (OpenAPI 3.1.x / JSON Schema Draft 2020-12).
     * If non-null, the value must be strictly less than this number.
     */
    private final BigDecimal exclusiveMaximumValue;
    
    /**
     * The exclusive minimum value constraint (OpenAPI 3.1.x / JSON Schema Draft 2020-12).
     * If non-null, the value must be strictly greater than this number.
     */
    private final BigDecimal exclusiveMinimumValue;

    /**
     * Constructs a number schema.
     * @param type the type
     * @param enumValues the enum values
     * @param schemaAttributes the attributes
     * @param schema the underlying schema
     * @param maximum the maximum constraint
     * @param minimum the minimum constraint
     * @param exclusiveMaximum the exclusiveMaximum constraint (deprecated, use exclusiveMaximumValue)
     * @param exclusiveMinimum the exclusiveMinimum constraint (deprecated, use exclusiveMinimumValue)
     * @deprecated Use constructor with exclusiveMaximumValue and exclusiveMinimumValue parameters
     */
    @Deprecated
    public NumberSchema(String type, Set<String> enumValues, Set<SchemaAttribute> schemaAttributes, Schema schema,
                        BigDecimal maximum, BigDecimal minimum, boolean exclusiveMaximum, boolean exclusiveMinimum) {
        super(type, enumValues, schemaAttributes, schema);
        this.maximum = maximum;
        this.minimum = minimum;
        this.exclusiveMaximum = exclusiveMaximum;
        this.exclusiveMinimum = exclusiveMinimum;
        // Convert boolean to numeric representation for backward compatibility
        this.exclusiveMaximumValue = exclusiveMaximum && maximum != null ? maximum : null;
        this.exclusiveMinimumValue = exclusiveMinimum && minimum != null ? minimum : null;
    }
    
    /**
     * Constructs a number schema with numeric exclusive bounds (OpenAPI 3.1.x).
     * @param type the type
     * @param enumValues the enum values
     * @param schemaAttributes the attributes
     * @param schema the underlying schema
     * @param maximum the maximum constraint
     * @param minimum the minimum constraint
     * @param exclusiveMaximumValue the exclusive maximum value (can be different from maximum)
     * @param exclusiveMinimumValue the exclusive minimum value (can be different from minimum)
     */
    public NumberSchema(String type, Set<String> enumValues, Set<SchemaAttribute> schemaAttributes, Schema schema,
                        BigDecimal maximum, BigDecimal minimum, BigDecimal exclusiveMaximumValue, BigDecimal exclusiveMinimumValue) {
        super(type, enumValues, schemaAttributes, schema);
        this.maximum = maximum;
        this.minimum = minimum;
        this.exclusiveMaximumValue = exclusiveMaximumValue;
        this.exclusiveMinimumValue = exclusiveMinimumValue;
        // Set boolean flags based on numeric values for backward compatibility
        this.exclusiveMaximum = exclusiveMaximumValue != null;
        this.exclusiveMinimum = exclusiveMinimumValue != null;
    }
}
