package com.docktape.swagger.brake.core.model.transformer;

import java.util.Collection;

import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.model.Request;
import com.docktape.swagger.brake.core.model.Response;
import com.docktape.swagger.brake.core.model.parameter.RequestParameter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class PathDetail {
    private final HttpMethod method;
    private final Request requestBody;
    private final Collection<RequestParameter> requestParameters;
    private final Collection<Response> responses;
    private final boolean deprecated;
    private final boolean betaApi;
}
