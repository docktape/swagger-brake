package com.docktape.swagger.brake.core.model.store;

import com.docktape.swagger.brake.core.model.transformer.Transformer;
import io.swagger.v3.oas.models.Components;
import org.springframework.stereotype.Component;

@Component
public class ResponsesTransformer implements Transformer<Components, ResponseStore> {
    @Override
    public ResponseStore transform(Components from) {
        return new ResponseStore(from.getResponses());
    }
}