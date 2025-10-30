package com.docktape.swagger.brake.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString
public class SchemaAttribute implements Comparable<SchemaAttribute> {
    private final String name;
    private final Schema schema;
    private final boolean required;
    private final boolean deprecated;

    public String getName() {
        return name.replace(Schema.LEVEL_DELIMITER_REPLACE_VALUE, Schema.LEVEL_DELIMITER);
    }

    public String getType() {
        return schema.getType();
    }

    @Override
    public int compareTo(SchemaAttribute o) {
        return name.compareTo(o.name);
    }
}
