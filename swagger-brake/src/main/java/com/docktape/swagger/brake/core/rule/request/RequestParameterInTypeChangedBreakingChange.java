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
public class RequestParameterInTypeChangedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String name;
    private final String oldType;
    private final String newType;

    @Override
    public String getMessage() {
        return format("%s parameter in type has been changed in %s %s from %s to %s", name, method, path, oldType, newType);
    }

    @Override
    public String getRuleCode() {
        return "R006";
    }
}
