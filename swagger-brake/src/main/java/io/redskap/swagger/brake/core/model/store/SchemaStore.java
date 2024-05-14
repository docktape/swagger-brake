package io.redskap.swagger.brake.core.model.store;

import io.swagger.v3.oas.models.media.Schema;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SchemaStore {
    private final Map<String, Schema> nativeSchemas;
    private final Map<String, io.redskap.swagger.brake.core.model.Schema> transformerSchemas = new ConcurrentHashMap<>();

    public Optional<Schema> getNative(String name) {
        return Optional.ofNullable(nativeSchemas.get(name));
    }

    public Optional<io.redskap.swagger.brake.core.model.Schema> getTransformer(String name, Supplier<io.redskap.swagger.brake.core.model.Schema> provider) {
        if (!transformerSchemas.containsKey(name)) {
            io.redskap.swagger.brake.core.model.Schema schema = provider.get();
            transformerSchemas.put(name, schema);
        }

        return Optional.ofNullable(transformerSchemas.get(name));

    }
}
