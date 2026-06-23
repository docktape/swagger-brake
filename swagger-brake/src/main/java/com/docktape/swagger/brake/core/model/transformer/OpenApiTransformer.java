package com.docktape.swagger.brake.core.model.transformer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.model.store.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenApiTransformer implements Transformer<OpenAPI, Specification> {
    private final PathTransformer pathTransformer;
    private final ComponentsTransformer componentsTransformer;
    private final ParametersTransformer parametersTransformer;
    private final ResponsesTransformer responsesTransformer;

    @Override
    public Specification transform(OpenAPI from) {
        if (from == null) {
            throw new IllegalArgumentException("input must not be null");
        }
        SchemaStore schemaStore = componentsTransformer.transform(from.getComponents());
        ParameterStore parameterStore = parametersTransformer.transform(from.getComponents());
        ResponseStore responseStore = responsesTransformer.transform(from.getComponents());
        Collection<Path> paths;
        try {
            StoreProvider.setSchemaStore(schemaStore);
            StoreProvider.setParameterStore(parameterStore);
            StoreProvider.setResponseStore(responseStore);
            paths = pathTransformer.transform(from.getPaths());
        } finally {
            StoreProvider.clear();
        }
        List<String> serverUrls = extractServerUrls(from);
        return new Specification(paths, serverUrls);
    }

    private List<String> extractServerUrls(OpenAPI from) {
        List<Server> servers = from.getServers();
        if (servers == null) {
            return Collections.emptyList();
        }
        return servers.stream()
            .map(Server::getUrl)
            .filter(url -> url != null)
            .collect(Collectors.toList());
    }
}
