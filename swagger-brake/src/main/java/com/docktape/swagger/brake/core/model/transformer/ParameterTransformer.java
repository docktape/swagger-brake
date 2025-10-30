package com.docktape.swagger.brake.core.model.transformer;

import com.docktape.swagger.brake.core.model.parameter.RequestParameter;
import com.docktape.swagger.brake.core.model.parameter.RequestParameterFactory;
import com.docktape.swagger.brake.core.model.service.TypeRefNameResolver;
import com.docktape.swagger.brake.core.model.store.ParameterStore;
import com.docktape.swagger.brake.core.model.store.StoreProvider;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParameterTransformer implements Transformer<Parameter, RequestParameter> {
    private final TypeRefNameResolver typeRefNameResolver;
    private final RequestParameterFactory requestParameterFactory;

    @Override
    public RequestParameter transform(Parameter from) {
        if (from.get$ref() != null) {
            from = resolveRef(from);
        }
        return requestParameterFactory.create(from);
    }

    private Parameter resolveRef(Parameter from) {
        ParameterStore parameterStore = StoreProvider.provideParameterStore();
        if (parameterStore == null) {
            throw new IllegalStateException("No ParameterStore available.");
        }
        String refName = typeRefNameResolver.resolve(from.get$ref());
        return parameterStore.get(refName).orElseThrow(() -> new IllegalStateException("Reference not found for " + refName));
    }
}
