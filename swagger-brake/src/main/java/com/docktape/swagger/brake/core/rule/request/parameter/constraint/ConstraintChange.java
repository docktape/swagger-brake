package com.docktape.swagger.brake.core.rule.request.parameter.constraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ConstraintChange {
    private final String attribute;
    private final Object oldValue;
    private final Object newValue;
}
