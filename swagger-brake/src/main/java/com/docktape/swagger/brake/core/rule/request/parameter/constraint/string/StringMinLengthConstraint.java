package com.docktape.swagger.brake.core.rule.request.parameter.constraint.string;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.StringConstrainedValue;
import org.springframework.stereotype.Component;

@Component
public class StringMinLengthConstraint implements Constraint<StringConstrainedValue> {
    public static final String MIN_LENGTH_ATTRIBUTE_NAME = "minLength";

    @Override
    public Optional<ConstraintChange> validateConstraints(StringConstrainedValue oldConstrainedValue, StringConstrainedValue newConstrainedValue) {
        ConstraintChange result = null;
        if (oldConstrainedValue != null && newConstrainedValue != null) {
            Integer oldMinLength = oldConstrainedValue.getMinLength();
            Integer newMinLength = newConstrainedValue.getMinLength();
            if (oldMinLength == null && newMinLength != null) {
                result = new ConstraintChange(
                    MIN_LENGTH_ATTRIBUTE_NAME, null, newMinLength
                );
            }
            if (oldMinLength != null && newMinLength != null) {
                if (oldMinLength < newMinLength) {
                    result = new ConstraintChange(
                        MIN_LENGTH_ATTRIBUTE_NAME, oldMinLength, newMinLength
                    );
                }
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Class<StringConstrainedValue> handledRequestParameter() {
        return StringConstrainedValue.class;
    }
}
