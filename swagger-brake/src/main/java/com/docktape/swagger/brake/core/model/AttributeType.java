package com.docktape.swagger.brake.core.model;

import java.util.*;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttributeType {
    GENERIC(null, null),
    NUMBER("number", null),
    FLOAT("number", "float"),
    DOUBLE("number", "double"),
    INTEGER("integer", null),
    INT_32("integer", "int32"),
    INT_64("integer", "int64"),
    STRING("string", null),
    ARRAY("array", null);

    private static final List<AttributeType> VALUES = Arrays.asList(AttributeType.values());

    private String type;
    private String format;

    /**
     * Creates an instance of the enum based on the type and format arguments. If there's no corresponding enum value, it falls back to GENERIC.
     * @param type the type
     * @param format the format
     * @return the actual enum type or GENERIC if there's none
     */
    public static AttributeType from(String type, String format) {
        return forTypeAndFormat(type, format)
            .orElse(forTypeAndFormat(type, null).orElse(GENERIC));
    }

    private static Optional<AttributeType> forTypeAndFormat(String type, String format) {
        for (AttributeType attributeType : VALUES) {
            if (Objects.equals(attributeType.getType(), type) && Objects.equals(attributeType.getFormat(), format)) {
                return Optional.of(attributeType);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns all number types.
     * @return all number types
     */
    public static Collection<AttributeType> getNumberTypes() {
        return ImmutableList.of(NUMBER, FLOAT, DOUBLE, INTEGER, INT_32, INT_64);
    }

    /**
     * Returns all string types.
     * @return Returns all string types
     */
    public static Collection<AttributeType> getStringTypes() {
        return ImmutableList.of(STRING);
    }

    /**
     * Returns all array types.
     * @return Returns all array types
     */
    public static Collection<AttributeType> getArrayTypes() {
        return ImmutableList.of(ARRAY);
    }
}
