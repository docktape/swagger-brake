package com.docktape.swagger.brake.core.model.store;

import com.docktape.swagger.brake.core.model.transformer.Transformer;
import io.swagger.v3.oas.models.Components;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComponentsTransformer implements Transformer<Components, SchemaStore> {
    @Override
    public SchemaStore transform(Components from) {
        return new SchemaStore(from.getSchemas());
    }
}
