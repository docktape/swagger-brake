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
public class RequestPropertyBecameNonNullableBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String propertyName;

    @Override
    public String getMessage() {
        return format("%s became non-nullable at %s %s", propertyName, method, path);
    }

    @Override
    public String getRuleCode() {
        return "R027";
    }
}
