package com.docktape.swagger.brake.core.rule.request.parameter.constraint;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class NumberConstrainedValue implements ConstrainedValue {
    private final BigDecimal maximum;
    private final BigDecimal minimum;
    private final boolean exclusiveMaximum;
    private final boolean exclusiveMinimum;
}
