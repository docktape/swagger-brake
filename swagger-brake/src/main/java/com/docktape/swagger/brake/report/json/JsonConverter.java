package com.docktape.swagger.brake.report.json;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonConverter {
    private final ObjectMapper objectMapper;

    /**
     * Convers an object to a JSON string representation.
     * @param obj the object to be converted. Must be not null.
     * @return the JSON representation of the object as a string.
     */
    public String convert(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convers an object to a {@link Map} representation.
     * @param obj the object to be converted. Must be not null.
     * @return the {@link Map} representation of the object.
     */
    public Map<String, Object> toMap(Object obj) {
        return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }
}
