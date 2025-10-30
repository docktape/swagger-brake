package com.docktape.swagger.brake.core.rule.request.parameter.constraint.array;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ArrayConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

@Component
public class ArrayUniqueItemsConstraint implements Constraint<ArrayConstrainedValue> {
    public static final String UNIQUE_ITEMS_ATTRIBUTE_NAME = "uniqueItems";

    @Override
    public Optional<ConstraintChange> validateConstraints(ArrayConstrainedValue oldRequestParameter, ArrayConstrainedValue newRequestParameter) {
        ConstraintChange result = null;
        if (oldRequestParameter != null && newRequestParameter != null) {
            Boolean oldUniqueItems = oldRequestParameter.getUniqueItems();
            Boolean newUniqueItems = newRequestParameter.getUniqueItems();
            if (BooleanUtils.isTrue(newUniqueItems) && BooleanUtils.isNotTrue(oldUniqueItems)) {
                result = new ConstraintChange(
                        UNIQUE_ITEMS_ATTRIBUTE_NAME,
                        oldUniqueItems,
                        newUniqueItems
                );
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Class<ArrayConstrainedValue> handledRequestParameter() {
        return ArrayConstrainedValue.class;
    }
}
