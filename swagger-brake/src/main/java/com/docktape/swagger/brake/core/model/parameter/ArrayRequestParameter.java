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
public class ArrayRequestParameter extends RequestParameter {
    private final Integer maxItems;
    private final Integer minItems;
    private final Boolean uniqueItems;

    /**
     * Constructor to create an instance.
     * @param inType the {@link RequestParameterInType}
     * @param name the name
     * @param required whether its required
     * @param schema the @{@link Schema}
     * @param requestParameterType the {@link AttributeType}
     * @param maxItems the maxLength
     * @param minItems the minLength
     * @param uniqueItems if only unique items are allowed
     */
    public ArrayRequestParameter(RequestParameterInType inType, String name,
                                 boolean required, Schema schema, AttributeType requestParameterType,
                                 Integer maxItems, Integer minItems, Boolean uniqueItems) {
        super(inType, name, required, schema, requestParameterType);
        this.maxItems = maxItems;
        this.minItems = minItems;
        this.uniqueItems = uniqueItems;
    }
}
