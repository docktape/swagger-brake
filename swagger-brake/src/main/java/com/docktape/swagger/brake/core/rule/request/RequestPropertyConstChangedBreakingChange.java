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
public class RequestPropertyConstChangedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String propertyName;
    private final String oldConst;
    private final String newConst;

    @Override
    public String getMessage() {
        return format("const of %s changed at %s %s", propertyName, method, path);
    }

    @Override
    public String getRuleCode() {
        return "R030";
    }
}
