package com.docktape.swagger.brake.core.rule.request.parameter.constraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ArrayConstrainedValue implements ConstrainedValue {
    private final Integer maxItems;
    private final Integer minItems;
    private final Boolean uniqueItems;
}
