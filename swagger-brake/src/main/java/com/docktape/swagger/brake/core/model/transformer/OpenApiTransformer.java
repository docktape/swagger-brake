package com.docktape.swagger.brake.core.model.transformer;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.model.store.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenApiTransformer implements Transformer<OpenAPI, Specification> {
    private final PathTransformer pathTransformer;
    private final PathItemTransformer pathItemTransformer;
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
        Collection<Path> webhooks;
        try {
            StoreProvider.setSchemaStore(schemaStore);
            StoreProvider.setParameterStore(parameterStore);
            StoreProvider.setResponseStore(responseStore);
            paths = pathTransformer.transform(from.getPaths());
            webhooks = transformWebhooks(from.getWebhooks());
        } finally {
            StoreProvider.clear();
        }
        List<String> serverUrls = extractServerUrls(from);
        return new Specification(paths, serverUrls, webhooks);
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

    private Collection<Path> transformWebhooks(Map<String, PathItem> webhookMap) {
        if (webhookMap == null || webhookMap.isEmpty()) {
            return Collections.emptyList();
        }
        return webhookMap.entrySet().stream()
            .flatMap(e -> pathItemTransformer.transform(e.getValue()).stream()
                .map(detail -> new Path(
                    e.getKey(),
                    detail.getMethod(),
                    detail.getRequestBody(),
                    detail.getRequestParameters(),
                    detail.getResponses(),
                    detail.isDeprecated(),
                    detail.isBetaApi()
                )))
            .collect(toList());
    }
}
