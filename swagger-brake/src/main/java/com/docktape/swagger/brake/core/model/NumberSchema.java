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
    private final boolean exclusiveMaximum;
    private final boolean exclusiveMinimum;

    /**
     * Constructs a number schema.
     * @param type the type
     * @param enumValues the enum values
     * @param schemaAttributes the attributes
     * @param schema the underlying schema
     * @param maximum the maximum constraint
     * @param minimum the minimum constraint
     * @param exclusiveMaximum the exclusiveMaximum constraint
     * @param exclusiveMinimum the exclusiveMinimum constraint
     */
    public NumberSchema(String type, Set<String> enumValues, Set<SchemaAttribute> schemaAttributes, Schema schema,
                        BigDecimal maximum, BigDecimal minimum, boolean exclusiveMaximum, boolean exclusiveMinimum) {
        super(type, enumValues, schemaAttributes, schema);
        this.maximum = maximum;
        this.minimum = minimum;
        this.exclusiveMaximum = exclusiveMaximum;
        this.exclusiveMinimum = exclusiveMinimum;
    }
}
