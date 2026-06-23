package com.docktape.swagger.brake.core.rule.response;

import static java.lang.String.format;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ResponseConstraintChangedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String responseCode;
    private final String attributeName;
    private final ConstraintChange constraintChange;

    @Override
    public String getMessage() {
        return format("Response constraint loosened at %s in %s at %s %s: %s was changed from %s to %s",
            attributeName, responseCode, method, path,
            constraintChange.getAttribute(), constraintChange.getOldValue(), constraintChange.getNewValue());
    }

    @Override
    public String getRuleCode() {
        return "R025";
    }
}
