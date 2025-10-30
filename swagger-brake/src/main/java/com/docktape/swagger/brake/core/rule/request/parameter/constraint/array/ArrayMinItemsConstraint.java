package com.docktape.swagger.brake.core.rule.request.parameter.constraint.array;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ArrayConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import org.springframework.stereotype.Component;

@Component
public class ArrayMinItemsConstraint implements Constraint<ArrayConstrainedValue> {
    public static final String MIN_ITEMS_ATTRIBUTE_NAME = "minItems";

    @Override
    public Optional<ConstraintChange> validateConstraints(ArrayConstrainedValue oldRequestParameter, ArrayConstrainedValue newRequestParameter) {
        ConstraintChange result = null;
        if (oldRequestParameter != null && newRequestParameter != null) {
            Integer oldMinItems = oldRequestParameter.getMinItems();
            Integer newMinItems = newRequestParameter.getMinItems();
            if (oldMinItems == null && newMinItems != null) {
                result = new ConstraintChange(
                    MIN_ITEMS_ATTRIBUTE_NAME, null, newMinItems
                );
            }
            if (oldMinItems != null && newMinItems != null) {
                if (oldMinItems < newMinItems) {
                    result = new ConstraintChange(
                        MIN_ITEMS_ATTRIBUTE_NAME, oldMinItems, newMinItems
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
