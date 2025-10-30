package com.docktape.swagger.brake.core.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import org.apache.commons.lang3.BooleanUtils;

public class SchemaBuilder {
    private static final TreeSet<?> EMPTY_TREE_SET = new TreeSet<>();

    private final String type;
    private String format;
    private Collection<String> enumValues;
    private Collection<SchemaAttribute> schemaAttributes;
    private Schema schema;
    private Integer maxLength;
    private Integer minLength;
    private Integer maxItems;
    private Integer minItems;
    private Boolean uniqueItems;
    private BigDecimal maximum;
    private BigDecimal minimum;
    private boolean exclusiveMaximum;
    private boolean exclusiveMinimum;

    public SchemaBuilder(String type) {
        this.type = type;
    }

    public SchemaBuilder enumValues(Collection<String> enumValues) {
        this.enumValues = enumValues;
        return this;
    }

    public SchemaBuilder schemaAttributes(Collection<SchemaAttribute> schemaAttributes) {
        this.schemaAttributes = schemaAttributes;
        return this;
    }

    public SchemaBuilder schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public SchemaBuilder format(String format) {
        this.format = format;
        return this;
    }

    public SchemaBuilder maxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public SchemaBuilder minLength(Integer minLength) {
        this.minLength = minLength;
        return this;
    }

    public SchemaBuilder maxItems(Integer maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    public SchemaBuilder minItems(Integer minItems) {
        this.minItems = minItems;
        return this;
    }

    public SchemaBuilder uniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
        return this;
    }

    public SchemaBuilder maximum(BigDecimal maximum) {
        this.maximum = maximum;
        return this;
    }

    public SchemaBuilder minimum(BigDecimal minimum) {
        this.minimum = minimum;
        return this;
    }

    public SchemaBuilder exclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = BooleanUtils.toBoolean(exclusiveMaximum);
        return this;
    }

    public SchemaBuilder exclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = BooleanUtils.toBoolean(exclusiveMinimum);
        return this;
    }

    /**
     * Builds a {@link Schema} instance.
     * @return the constructed {@link Schema} instance.
     */
    public Schema build() {
        Collection<SchemaAttribute> attributes = Collections.emptyList();
        if (schemaAttributes != null) {
            attributes = schemaAttributes;
        }
        Collection<String> enValues = Collections.emptyList();
        if (enumValues != null) {
            enValues = enumValues;
        }
        AttributeType attributeType = AttributeType.from(type, format);
        TreeSet<String> enumValues;
        if (enValues.isEmpty()) {
            enumValues = (TreeSet<String>) EMPTY_TREE_SET;
        } else {
            enumValues = new TreeSet<>(enValues);
        }
        TreeSet<SchemaAttribute> schemaAttributes;
        if (attributes.isEmpty()) {
            schemaAttributes = (TreeSet<SchemaAttribute>) EMPTY_TREE_SET;
        } else {
            schemaAttributes = new TreeSet<>(attributes);
        }
        if (AttributeType.getStringTypes().contains(attributeType)) {
            return new StringSchema(type, enumValues, schemaAttributes, schema, maxLength, minLength);
        } else if (AttributeType.getNumberTypes().contains(attributeType)) {
            return new NumberSchema(type, enumValues, schemaAttributes, schema, maximum, minimum, exclusiveMaximum, exclusiveMinimum);
        } else if (AttributeType.getArrayTypes().contains(attributeType)) {
            return new ArraySchema(type, enumValues, schemaAttributes, schema, maxItems, minItems, uniqueItems);
        } else {
            return new Schema(type, enumValues, schemaAttributes, schema);
        }
    }
}
