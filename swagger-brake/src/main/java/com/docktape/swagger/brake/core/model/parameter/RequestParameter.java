package com.docktape.swagger.brake.core.model.parameter;

import java.util.Optional;

import com.docktape.swagger.brake.core.model.AttributeType;
import com.docktape.swagger.brake.core.model.RequestParameterInType;
import com.docktape.swagger.brake.core.model.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@SuperBuilder
public class RequestParameter {
    private final RequestParameterInType inType;
    private final String name;
    private final boolean required;
    private final Schema schema;
    private final AttributeType requestParameterType;
    private final String defaultValue;

    public RequestParameter(RequestParameterInType inType, String name, boolean required, AttributeType requestParameterType) {
        this(inType, name, required, null, requestParameterType, null);
    }

    public RequestParameter(RequestParameterInType inType, String name, boolean required, Schema schema, AttributeType requestParameterType) {
        this(inType, name, required, schema, requestParameterType, null);
    }

    public Optional<Schema> getSchema() {
        return Optional.ofNullable(schema);
    }
}
