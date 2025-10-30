package com.docktape.swagger.brake.core.rule.request.parameter.constraint.number;

import java.math.BigDecimal;
import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.NumberConstrainedValue;
import com.docktape.swagger.brake.core.util.BigDecimalComparator;
import org.springframework.stereotype.Component;

@Component
class NumberMaximumConstraint implements Constraint<NumberConstrainedValue> {
    public static final String MAXIMUM_ATTRIBUTE_NAME = "maximum";
    public static final String EXCLUSIVE_MAXIMUM_ATTRIBUTE_NAME = "exclusiveMaximum";

    @Override
    public Optional<ConstraintChange> validateConstraints(NumberConstrainedValue oldRequestParameter, NumberConstrainedValue newRequestParameter) {
        ConstraintChange result = null;
        if (oldRequestParameter != null && newRequestParameter != null) {
            BigDecimal oldMaximum = oldRequestParameter.getMaximum();
            BigDecimal newMaximum = newRequestParameter.getMaximum();
            if (oldMaximum == null && newMaximum != null) {
                result = new ConstraintChange(MAXIMUM_ATTRIBUTE_NAME,
                    null,
                    new PrettyFormattedBigDecimal(newMaximum)
                );
            }
            if (oldMaximum != null && newMaximum != null) {
                boolean oldExclusiveMaximum = oldRequestParameter.isExclusiveMaximum();
                boolean newExclusiveMaximum = newRequestParameter.isExclusiveMaximum();
                if (oldExclusiveMaximum == newExclusiveMaximum && BigDecimalComparator.isLessThan(newMaximum, oldMaximum)) {
                    result = new ConstraintChange(MAXIMUM_ATTRIBUTE_NAME,
                        new PrettyFormattedBigDecimal(oldMaximum),
                        new PrettyFormattedBigDecimal(newMaximum)
                    );
                } else {
                    BigDecimal oldMaxWithExclusivity = oldMaximum.subtract(oldExclusiveMaximum ? BigDecimal.ONE : BigDecimal.ZERO);
                    BigDecimal newMaxWithExclusivity = newMaximum.subtract(newExclusiveMaximum ? BigDecimal.ONE : BigDecimal.ZERO);
                    if (BigDecimalComparator.isLessThan(newMaxWithExclusivity, oldMaxWithExclusivity)) {
                        result = new ConstraintChange(EXCLUSIVE_MAXIMUM_ATTRIBUTE_NAME,
                            oldExclusiveMaximum,
                            newExclusiveMaximum
                        );
                    }
                }
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Class<NumberConstrainedValue> handledRequestParameter() {
        return NumberConstrainedValue.class;
    }
}
