package com.docktape.swagger.brake.core.rule.request.parameter.constraint.string;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.StringConstrainedValue;
import org.springframework.stereotype.Component;

@Component
public class StringMaxLengthConstraint implements Constraint<StringConstrainedValue> {
    public static final String MAX_LENGTH_ATTRIBUTE_NAME = "maxLength";

    @Override
    public Optional<ConstraintChange> validateConstraints(StringConstrainedValue oldConstrainedValue, StringConstrainedValue newConstrainedValue) {
        ConstraintChange result = null;
        if (oldConstrainedValue != null && newConstrainedValue != null) {
            Integer oldMaxLength = oldConstrainedValue.getMaxLength();
            Integer newMaxLength = newConstrainedValue.getMaxLength();
            if (oldMaxLength == null && newMaxLength != null) {
                result = new ConstraintChange(
                        MAX_LENGTH_ATTRIBUTE_NAME, null, newMaxLength
                );
            }
            if (oldMaxLength != null && newMaxLength != null) {
                if (newMaxLength < oldMaxLength) {
                    result = new ConstraintChange(
                            MAX_LENGTH_ATTRIBUTE_NAME, oldMaxLength, newMaxLength
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
