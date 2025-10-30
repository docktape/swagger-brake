package com.docktape.swagger.brake.core.model.store;

import com.docktape.swagger.brake.core.model.transformer.Transformer;
import io.swagger.v3.oas.models.Components;
import org.springframework.stereotype.Component;

@Component
public class ParametersTransformer implements Transformer<Components, ParameterStore> {
    @Override
    public ParameterStore transform(Components from) {
        return new ParameterStore(from.getParameters());
    }
}
