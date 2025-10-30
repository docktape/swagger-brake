package com.docktape.swagger.brake.core.model;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ArraySchema extends Schema {
    private final Integer maxItems;
    private final Integer minItems;
    private final Boolean uniqueItems;

    /**
     * Constructs an array schema.
     * @param type the type
     * @param enumValues the enum values
     * @param schemaAttributes the attributes
     * @param schema the underlying schema
     * @param maxItems the maxItems constraint
     * @param minItems the minItems constraint
     * @param uniqueItems the uniqueItems constraint
     */
    public ArraySchema(String type, Set<String> enumValues, Set<SchemaAttribute> schemaAttributes, Schema schema, Integer maxItems, Integer minItems, Boolean uniqueItems) {
        super(type, enumValues, schemaAttributes, schema);
        this.maxItems = maxItems;
        this.minItems = minItems;
        this.uniqueItems = uniqueItems;
    }
}
