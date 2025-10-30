package com.docktape.swagger.brake.core.model;

import java.util.Collection;
import java.util.Optional;

import com.docktape.swagger.brake.core.model.parameter.RequestParameter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Path {
    private final String path;
    private final HttpMethod method;
    private final Request requestBody;
    private final Collection<RequestParameter> requestParameters;
    private final Collection<Response> responses;
    private final boolean deprecated;
    private final boolean betaApi;

    public Optional<Response> getResponseByCode(String code) {
        return responses.stream().filter(r -> code.equals(r.getCode())).findAny();
    }

    public Optional<RequestParameter> getRequestParameterByName(String name) {
        return requestParameters.stream().filter(p -> name.equals(p.getName())).findAny();
    }

    public Optional<Request> getRequestBody() {
        return Optional.ofNullable(requestBody);
    }
}
