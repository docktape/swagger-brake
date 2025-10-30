package com.docktape.swagger.brake.core.rule.request.parameter.constraint.array;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ArrayConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import org.springframework.stereotype.Component;

@Component
public class ArrayMaxItemsConstraint implements Constraint<ArrayConstrainedValue> {
    public static final String MAX_ITEMS_ATTRIBUTE_NAME = "maxItems";

    @Override
    public Optional<ConstraintChange> validateConstraints(ArrayConstrainedValue oldRequestParameter, ArrayConstrainedValue newRequestParameter) {
        ConstraintChange result = null;
        if (oldRequestParameter != null && newRequestParameter != null) {
            Integer oldMaxItems = oldRequestParameter.getMaxItems();
            Integer newMaxItems = newRequestParameter.getMaxItems();
            if (oldMaxItems == null && newMaxItems != null) {
                result = new ConstraintChange(
                    MAX_ITEMS_ATTRIBUTE_NAME, null, newMaxItems
                );
            }
            if (oldMaxItems != null && newMaxItems != null) {
                if (newMaxItems < oldMaxItems) {
                    result = new ConstraintChange(
                        MAX_ITEMS_ATTRIBUTE_NAME, oldMaxItems, newMaxItems
                    );
                }
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Class<ArrayConstrainedValue> handledRequestParameter() {
        return ArrayConstrainedValue.class;
    }
}
