package com.docktape.swagger.brake.core.rule.request.parameter.constraint.number;

import java.math.BigDecimal;
import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.NumberConstrainedValue;
import com.docktape.swagger.brake.core.util.BigDecimalComparator;
import org.springframework.stereotype.Component;

@Component
class NumberMinimumConstraint implements Constraint<NumberConstrainedValue> {
    public static final String MINIMUM_ATTRIBUTE_NAME = "minimum";
    public static final String EXCLUSIVE_MINIMUM_ATTRIBUTE_NAME = "exclusiveMinimum";

    @Override
    public Optional<ConstraintChange> validateConstraints(NumberConstrainedValue oldRequestParameter, NumberConstrainedValue newRequestParameter) {
        ConstraintChange result = null;
        if (oldRequestParameter != null && newRequestParameter != null) {
            BigDecimal oldMinimum = oldRequestParameter.getMinimum();
            BigDecimal newMinimum = newRequestParameter.getMinimum();
            if (oldMinimum == null && newMinimum != null) {
                result = new ConstraintChange(MINIMUM_ATTRIBUTE_NAME,
                    null,
                    new PrettyFormattedBigDecimal(newMinimum)
                );
            }
            if (oldMinimum != null && newMinimum != null) {
                boolean oldExclusiveMinimum = oldRequestParameter.isExclusiveMinimum();
                boolean newExclusiveMinimum = newRequestParameter.isExclusiveMinimum();
                if (oldExclusiveMinimum == newExclusiveMinimum && BigDecimalComparator.isGreaterThan(newMinimum, oldMinimum)) {
                    result = new ConstraintChange(MINIMUM_ATTRIBUTE_NAME,
                        new PrettyFormattedBigDecimal(oldMinimum),
                        new PrettyFormattedBigDecimal(newMinimum)
                    );
                } else {
                    BigDecimal oldMaxWithExclusivity = oldMinimum.add(oldExclusiveMinimum ? BigDecimal.ONE : BigDecimal.ZERO);
                    BigDecimal newMaxWithExclusivity = newMinimum.add(newExclusiveMinimum ? BigDecimal.ONE : BigDecimal.ZERO);
                    if (BigDecimalComparator.isGreaterThan(newMaxWithExclusivity, oldMaxWithExclusivity)) {
                        result = new ConstraintChange(EXCLUSIVE_MINIMUM_ATTRIBUTE_NAME,
                            oldExclusiveMinimum,
                            newExclusiveMinimum
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
