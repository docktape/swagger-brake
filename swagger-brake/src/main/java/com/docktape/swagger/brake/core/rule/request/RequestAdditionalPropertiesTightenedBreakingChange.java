package com.docktape.swagger.brake.core.rule.request;

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
public class RequestAdditionalPropertiesTightenedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String schemaPath;

    @Override
    public String getMessage() {
        return String.format("additionalProperties was tightened at %s in %s %s", schemaPath, method, path);
    }

    @Override
    public String getRuleCode() {
        return "R029";
    }
}
