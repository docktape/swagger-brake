package com.docktape.swagger.brake.core.model.transformer;

import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.function.Function;

import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.model.Request;
import com.docktape.swagger.brake.core.model.Response;
import com.docktape.swagger.brake.core.model.parameter.RequestParameter;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PathItemTransformer implements Transformer<PathItem, Collection<PathDetail>> {
    private static final Map<HttpMethod, Function<PathItem, Operation>> MAPPERS = new HashMap<>();

    static {
        MAPPERS.put(HttpMethod.GET, PathItem::getGet);
        MAPPERS.put(HttpMethod.POST, PathItem::getPost);
        MAPPERS.put(HttpMethod.PUT, PathItem::getPut);
        MAPPERS.put(HttpMethod.PATCH, PathItem::getPatch);
        MAPPERS.put(HttpMethod.DELETE, PathItem::getDelete);
        MAPPERS.put(HttpMethod.HEAD, PathItem::getHead);
        MAPPERS.put(HttpMethod.OPTIONS, PathItem::getOptions);
        MAPPERS.put(HttpMethod.TRACE, PathItem::getTrace);
    }

    private final ApiResponseTransformer apiResponseTransformer;
    private final ParameterTransformer parameterTransformer;
    private final RequestBodyTransformer requestBodyTransformer;
    private final CheckerOptionsProvider checkerOptionsProvider;

    @Override
    public Collection<PathDetail> transform(PathItem from) {
        Collection<PathDetail> result = new ArrayList<>();
        for (Map.Entry<HttpMethod, Function<PathItem, Operation>> e : MAPPERS.entrySet()) {
            Operation operation = e.getValue().apply(from);
            if (operation != null) {
                HttpMethod key = e.getKey();

                boolean isDeprecated = BooleanUtils.isTrue(operation.getDeprecated());
                boolean isBetaApi = getBetaApiValue(operation);
                Request requestBody = getRequestBody(operation);
                List<RequestParameter> requestParameters = getRequestParameters(operation);
                List<Response> responses = getResponses(operation);
                PathDetail detail = new PathDetail(key, requestBody, requestParameters, responses, isDeprecated, isBetaApi);
                result.add(detail);
            }
        }
        return result;
    }

    private boolean getBetaApiValue(Operation operation) {
        Map<String, Object> extensions = operation.getExtensions();
        if (extensions != null) {
            Object betaApiAttribute = extensions.get(checkerOptionsProvider.get().getBetaApiExtensionName());
            if (betaApiAttribute != null) {
                return BooleanUtils.toBoolean(betaApiAttribute.toString());
            }
        }
        return false;
    }

    private Request getRequestBody(Operation operation) {
        Request result = null;
        RequestBody requestBody = operation.getRequestBody();
        if (requestBody != null) {
            result = requestBodyTransformer.transform(requestBody);
        }
        return result;
    }

    private List<RequestParameter> getRequestParameters(Operation operation) {
        List<RequestParameter> result = Collections.emptyList();
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) {
            result = parameters.stream().map(parameterTransformer::transform).collect(toList());
        }
        return result;
    }

    private List<Response> getResponses(Operation operation) {
        return operation.getResponses().entrySet().stream()
            .map(entry -> new ImmutablePair<>(entry.getKey(), entry.getValue()))
            .map(apiResponseTransformer::transform)
            .collect(toList());
    }
}