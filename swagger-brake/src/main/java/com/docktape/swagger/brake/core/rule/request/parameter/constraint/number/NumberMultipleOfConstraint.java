package com.docktape.swagger.brake.core.rule.request.parameter.constraint.number;

import java.math.BigDecimal;
import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.NumberConstrainedValue;
import org.springframework.stereotype.Component;

@Component
class NumberMultipleOfConstraint implements Constraint<NumberConstrainedValue> {
    public static final String MULTIPLE_OF_ATTRIBUTE_NAME = "multipleOf";

    @Override
    public Optional<ConstraintChange> validateConstraints(NumberConstrainedValue oldRequestParameter, NumberConstrainedValue newRequestParameter) {
        ConstraintChange result = null;
        if (oldRequestParameter != null && newRequestParameter != null) {
            BigDecimal oldMultipleOf = oldRequestParameter.getMultipleOf();
            BigDecimal newMultipleOf = newRequestParameter.getMultipleOf();
            if (oldMultipleOf == null && newMultipleOf != null) {
                result = new ConstraintChange(MULTIPLE_OF_ATTRIBUTE_NAME,
                    null,
                    new PrettyFormattedBigDecimal(newMultipleOf)
                );
            } else if (oldMultipleOf != null && newMultipleOf != null && oldMultipleOf.compareTo(newMultipleOf) != 0) {
                result = new ConstraintChange(MULTIPLE_OF_ATTRIBUTE_NAME,
                    new PrettyFormattedBigDecimal(oldMultipleOf),
                    new PrettyFormattedBigDecimal(newMultipleOf)
                );
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Class<NumberConstrainedValue> handledRequestParameter() {
        return NumberConstrainedValue.class;
    }
}
