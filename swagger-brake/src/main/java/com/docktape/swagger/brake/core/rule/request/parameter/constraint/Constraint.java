package com.docktape.swagger.brake.core.rule.request.parameter.constraint;

import java.util.Optional;

public interface Constraint<T extends ConstrainedValue> {
    Optional<ConstraintChange> validateConstraints(T oldRequestParameter, T newRequestParameter);

    Class<T> handledRequestParameter();
}
