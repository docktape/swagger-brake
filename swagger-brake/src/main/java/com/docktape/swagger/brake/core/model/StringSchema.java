package com.docktape.swagger.brake.core.model;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class StringSchema extends Schema {
    private final Integer maxLength;
    private final Integer minLength;
    private final String pattern;

    /**
     * Constructs a string schema.
     * @param type the type
     * @param enumValues the enum values
     * @param schemaAttributes the attributes
     * @param schema the underlying schema
     * @param maxLength the maxLength constraint
     * @param minLength the minLength constraint
     * @param extEnum the x-extensible-enum values
     * @param constValue the const value (nullable)
     * @param pattern the pattern constraint
     */
    public StringSchema(String type, Set<String> enumValues, Set<SchemaAttribute> schemaAttributes, Schema schema,
                        Integer maxLength, Integer minLength, Set<String> extEnum, String constValue, String pattern) {
        super(type, enumValues, schemaAttributes, schema, extEnum, constValue);
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.pattern = pattern;
    }
}
