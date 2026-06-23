package com.docktape.swagger.brake.core.rule.request.parameter.constraint.string;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.StringConstrainedValue;
import org.springframework.stereotype.Component;

@Component
public class StringPatternConstraint implements Constraint<StringConstrainedValue> {
    public static final String PATTERN_ATTRIBUTE_NAME = "pattern";

    @Override
    public Optional<ConstraintChange> validateConstraints(StringConstrainedValue oldConstrainedValue, StringConstrainedValue newConstrainedValue) {
        ConstraintChange result = null;
        if (oldConstrainedValue != null && newConstrainedValue != null) {
            String oldPattern = oldConstrainedValue.getPattern();
            String newPattern = newConstrainedValue.getPattern();
            if (oldPattern == null && newPattern != null) {
                result = new ConstraintChange(
                    PATTERN_ATTRIBUTE_NAME, null, newPattern
                );
            } else if (oldPattern != null && newPattern != null && !oldPattern.equals(newPattern)) {
                result = new ConstraintChange(
                    PATTERN_ATTRIBUTE_NAME, oldPattern, newPattern
                );
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Class<StringConstrainedValue> handledRequestParameter() {
        return StringConstrainedValue.class;
    }
}
