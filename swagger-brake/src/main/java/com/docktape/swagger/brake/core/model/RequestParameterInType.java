package com.docktape.swagger.brake.core.model;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RequestParameterInType {
    QUERY("query"),
    PATH("path"),
    HEADER("header");

    private final String name;

    public String getName() {
        return name;
    }

    /**
     * Convers a string value into a {@link RequestParameterInType} instance.
     * @param name the name of the enum.
     * @return the {@link RequestParameterInType} instance.
     */
    public static RequestParameterInType fromName(String name) {
        return Arrays.stream(RequestParameterInType.values())
            .filter(t -> name.equals(t.getName()))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Name " + name + " cannot be resolved"));
    }
}
