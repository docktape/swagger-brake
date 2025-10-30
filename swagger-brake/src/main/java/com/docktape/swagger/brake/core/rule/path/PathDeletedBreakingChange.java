package com.docktape.swagger.brake.core.rule.path;

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
public class PathDeletedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;

    @Override
    public String getMessage() {
        return format("Path %s %s has been deleted", path, method);
    }

    @Override
    public String getRuleCode() {
        return "R002";
    }
}
