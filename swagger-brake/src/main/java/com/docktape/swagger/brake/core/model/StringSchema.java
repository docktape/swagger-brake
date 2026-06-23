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

    /**
     * Constructs a string schema.
     * @param type the type
     * @param enumValues the enum values
     * @param schemaAttributes the attributes
     * @param schema the underlying schema
     * @param maxLength the maxLength constraint
     * @param minLength the minLength constraint
     * @param extEnum the x-extensible-enum values
     */
    public StringSchema(String type, Set<String> enumValues, Set<SchemaAttribute> schemaAttributes, Schema schema,
                        Integer maxLength, Integer minLength, Set<String> extEnum) {
        super(type, enumValues, schemaAttributes, schema, extEnum);
        this.maxLength = maxLength;
        this.minLength = minLength;
    }
}
