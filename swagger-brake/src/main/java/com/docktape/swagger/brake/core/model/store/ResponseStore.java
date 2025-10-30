package com.docktape.swagger.brake.core.model.store;

import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResponseStore {
    private final Map<String, ApiResponse> responses;

    public Optional<ApiResponse> get(String name) {
        return Optional.ofNullable(responses.get(name));
    }
}
