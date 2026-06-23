package com.docktape.swagger.brake.core.rule.response;

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
public class ResponsePropertyBecameOptionalBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String responseCode;
    private final String propertyName;

    @Override
    public String getMessage() {
        return format("%s in response %s at %s %s is no longer required", propertyName, responseCode, method, path);
    }

    @Override
    public String getRuleCode() {
        return "R022";
    }
}
