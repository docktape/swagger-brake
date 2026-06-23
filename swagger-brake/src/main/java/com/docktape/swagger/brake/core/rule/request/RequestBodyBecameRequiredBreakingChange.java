package com.docktape.swagger.brake.core.rule.request;

import static java.lang.String.format;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestBodyBecameRequiredBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;

    @Override
    public String getMessage() {
        return format("Request body became required at %s %s", method, path);
    }

    @Override
    public String getRuleCode() {
        return "R023";
    }
}
