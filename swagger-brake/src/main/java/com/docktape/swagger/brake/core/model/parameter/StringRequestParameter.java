package com.docktape.swagger.brake.core.model.parameter;

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
public class StringRequestParameter extends RequestParameter {
    private Integer maxLength;
    private Integer minLength;

    /**
     * Constructor to create an instance.
     * @param inType the {@link RequestParameterInType}
     * @param name the name
     * @param required whether its required
     * @param schema the @{@link Schema}
     * @param requestParameterType the {@link AttributeType}
     * @param maxLength the maximum length
     * @param minLength the minimum length
     */
    public StringRequestParameter(RequestParameterInType inType, String name,
                                  boolean required, Schema schema, AttributeType requestParameterType,
                                  Integer maxLength, Integer minLength) {
        super(inType, name, required, schema, requestParameterType);
        this.maxLength = maxLength;
        this.minLength = minLength;
    }
}
