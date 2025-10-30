package com.docktape.swagger.brake.core.model.parameter;

import static com.docktape.swagger.brake.core.model.AttributeType.GENERIC;

import java.math.BigDecimal;

import com.docktape.swagger.brake.core.model.AttributeType;
import com.docktape.swagger.brake.core.model.RequestParameterInType;
import com.docktape.swagger.brake.core.model.service.RequestParameterInTypeResolver;
import com.docktape.swagger.brake.core.model.transformer.SchemaTransformer;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestParameterFactory {
    private final SchemaTransformer schemaTransformer;
    private final RequestParameterInTypeResolver requestParameterInTypeResolver;

    /**
     * Creates a new @{@link RequestParameter} instance. Based on the incoming argument,
     * it could return any of the @{@link RequestParameter} subclasses.
     * @param from the @{@link Parameter} instance
     * @return a @{@link RequestParameter} or any of its subclasses
     */
    public RequestParameter create(Parameter from) {
        RequestParameterInType inType = requestParameterInTypeResolver.resolve(from.getIn());
        String name = from.getName();
        boolean required = BooleanUtils.toBoolean(from.getRequired());
        Schema swSchema = from.getSchema();
        if (swSchema != null) {
            // Validation for detecting when a request parameter is used with an actual schema object
            // even though its forbidden by the spec
            // https://github.com/docktape/swagger-brake/issues/28
            String type = swSchema.getType();
            if (type == null) {
                throw new IllegalStateException("schema does not have any type");
            }
            String format = swSchema.getFormat();
            AttributeType requestParameterType = AttributeType.from(type, format);
            com.docktape.swagger.brake.core.model.Schema transformedSchema = schemaTransformer.transform(swSchema);
            if (AttributeType.getNumberTypes().contains(requestParameterType)) {
                BigDecimal maximum = swSchema.getMaximum();
                Boolean exclusiveMaximum = swSchema.getExclusiveMaximum();
                BigDecimal minimum = swSchema.getMinimum();
                Boolean exclusiveMinimum = swSchema.getExclusiveMinimum();
                return NumberRequestParameter.builder()
                    .inType(inType)
                    .name(name)
                    .required(required)
                    .requestParameterType(requestParameterType)
                    .schema(transformedSchema)
                    .maximum(maximum)
                    .exclusiveMaximum(BooleanUtils.toBoolean(exclusiveMaximum))
                    .minimum(minimum)
                    .exclusiveMinimum(BooleanUtils.toBoolean(exclusiveMinimum))
                    .build();
            } else if (AttributeType.getStringTypes().contains(requestParameterType)) {
                Integer maxLength = swSchema.getMaxLength();
                Integer minLength = swSchema.getMinLength();
                return StringRequestParameter.builder()
                    .inType(inType)
                    .name(name)
                    .required(required)
                    .requestParameterType(requestParameterType)
                    .schema(transformedSchema)
                    .maxLength(maxLength)
                    .minLength(minLength)
                    .build();
            } else if (AttributeType.getArrayTypes().contains(requestParameterType)) {
                Integer maxItems = swSchema.getMaxItems();
                Integer minItems = swSchema.getMinItems();
                Boolean uniqueItems = swSchema.getUniqueItems();
                return ArrayRequestParameter.builder()
                    .inType(inType)
                    .name(name)
                    .required(required)
                    .requestParameterType(requestParameterType)
                    .schema(transformedSchema)
                    .maxItems(maxItems)
                    .minItems(minItems)
                    .uniqueItems(uniqueItems)
                    .build();

            } else {
                return RequestParameter.builder()
                    .inType(inType)
                    .name(name)
                    .required(required)
                    .requestParameterType(GENERIC)
                    .schema(transformedSchema)
                    .build();
            }
        }
        return RequestParameter.builder()
            .inType(inType)
            .name(name)
            .required(required)
            .requestParameterType(GENERIC)
            .build();
    }
}
