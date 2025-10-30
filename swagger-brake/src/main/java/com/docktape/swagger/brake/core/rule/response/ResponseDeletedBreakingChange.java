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
public class ResponseDeletedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String code;

    @Override
    public String getMessage() {
        return format("Response %s has been removed from %s %s", code, method, path);
    }

    @Override
    public String getRuleCode() {
        return "R012";
    }
}
