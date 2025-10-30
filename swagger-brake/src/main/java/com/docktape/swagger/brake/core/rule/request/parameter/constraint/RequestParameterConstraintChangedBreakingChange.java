package com.docktape.swagger.brake.core.rule.request.parameter.constraint;

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
public class RequestParameterConstraintChangedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String attributeName;
    private final ConstraintChange constraintChange;

    @Override
    public String getMessage() {
        return format("The %s constraint for attribute %s was changed in %s %s from %s to %s ", constraintChange.getAttribute(),
            attributeName, method, path, constraintChange.getOldValue(), constraintChange.getNewValue());
    }

    @Override
    public String getRuleCode() {
        return "R017";
    }
}
