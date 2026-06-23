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
public class RequestTypeXExtensibleEnumValueDeletedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String attributeName;
    private final String enumValue;

    @Override
    public String getMessage() {
        return format("%s was removed from x-extensible-enum of %s at %s %s", enumValue, attributeName, method, path);
    }

    @Override
    public String getRuleCode() {
        return "R024";
    }
}
