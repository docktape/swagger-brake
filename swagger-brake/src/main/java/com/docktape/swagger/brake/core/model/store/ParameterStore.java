package com.docktape.swagger.brake.core.model.store;

import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParameterStore {
    private final Map<String, Parameter> parameters;

    public Optional<Parameter> get(String name) {
        return Optional.ofNullable(parameters.get(name));
    }
}
