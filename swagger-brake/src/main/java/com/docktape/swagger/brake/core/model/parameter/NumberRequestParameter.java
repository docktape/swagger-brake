package com.docktape.swagger.brake.core.model.parameter;

import java.math.BigDecimal;

import com.docktape.swagger.brake.core.model.AttributeType;
import com.docktape.swagger.brake.core.model.RequestParameterInType;
import com.docktape.swagger.brake.core.model.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
public class NumberRequestParameter extends RequestParameter {
    private BigDecimal maximum;
    private BigDecimal minimum;
    private boolean exclusiveMaximum;
    private boolean exclusiveMinimum;

    /**
     * Constructor to create an instance.
     * @param inType the {@link RequestParameterInType}
     * @param name the name
     * @param required whether its required
     * @param schema the @{@link Schema}
     * @param requestParameterType the {@link AttributeType}
     * @param maximum the maximum
     * @param minimum the minimum
     * @param exclusiveMaximum whether its exclusively maximized
     * @param exclusiveMinimum whether its exclusively minimized
     */
    public NumberRequestParameter(RequestParameterInType inType, String name,
                                  boolean required, Schema schema, AttributeType requestParameterType,
                                  BigDecimal maximum, BigDecimal minimum,
                                  boolean exclusiveMaximum, boolean exclusiveMinimum) {
        super(inType, name, required, schema, requestParameterType);
        this.maximum = maximum;
        this.minimum = minimum;
        this.exclusiveMaximum = exclusiveMaximum;
        this.exclusiveMinimum = exclusiveMinimum;
    }

    /**
     * Determines if the object is number typed or not.
     * @return whether the object is number typed
     */
    public boolean isNumberTyped() {
        return AttributeType.getNumberTypes().contains(getRequestParameterType());
    }
}
