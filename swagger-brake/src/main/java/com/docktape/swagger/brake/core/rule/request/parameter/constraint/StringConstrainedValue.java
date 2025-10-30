package com.docktape.swagger.brake.core.rule.request.parameter.constraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class StringConstrainedValue implements ConstrainedValue {
    private final Integer maxLength;
    private final Integer minLength;
}
