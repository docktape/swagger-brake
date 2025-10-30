package com.docktape.swagger.brake.core.rule.request.parameter.constraint;

import java.util.*;
import java.util.stream.Collectors;

import com.docktape.swagger.brake.core.model.*;
import com.docktape.swagger.brake.core.model.parameter.ArrayRequestParameter;
import com.docktape.swagger.brake.core.model.parameter.NumberRequestParameter;
import com.docktape.swagger.brake.core.model.parameter.RequestParameter;
import com.docktape.swagger.brake.core.model.parameter.StringRequestParameter;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestParameterConstraintChangeRule implements BreakingChangeRule<RequestParameterConstraintChangedBreakingChange> {
    private final Collection<Constraint<?>> constraints;
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestParameterConstraintChangedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestParameterConstraintChangedBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                if (CollectionUtils.isNotEmpty(path.getRequestParameters())) {
                    for (RequestParameter requestParameter : path.getRequestParameters()) {
                        Optional<RequestParameter> newRequestParameter = newPath.getRequestParameterByName(requestParameter.getName());
                        if (newRequestParameter.isPresent()) {
                            RequestParameter newRequestParam = newRequestParameter.get();
                            breakingChanges.addAll(applyConstraints(path, fromRequestParam(requestParameter), fromRequestParam(newRequestParam), newRequestParam.getName()));
                        }
                    }
                }

                Optional<Request> requestBody = path.getRequestBody();
                Optional<Request> newRequestBody = newPath.getRequestBody();
                if (requestBody.isPresent() && newRequestBody.isPresent()) {
                    for (MediaType oldMediaType : requestBody.get().getMediaTypes().keySet()) {
                        Optional<Schema> newSchema = newRequestBody.get().getSchemaByMediaType(oldMediaType);
                        if (newSchema.isPresent()) {
                            Schema schema = newSchema.get();
                            Map<String, Schema> oldSchemas = requestBody.get().getSchemaByMediaType(oldMediaType).get().getSchemasRecursively();
                            Map<String, Schema> schemasRecursively = schema.getSchemasRecursively();
                            for (Map.Entry<String, Schema> entry : schemasRecursively.entrySet()) {
                                Schema oldSchema = oldSchemas.get(entry.getKey());
                                if (oldSchema != null) {
                                    breakingChanges.addAll(applyConstraints(path, fromSchema(oldSchema), fromSchema(entry.getValue()), entry.getKey()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return breakingChanges;
    }

    private ConstrainedValue fromSchema(Schema schema) {
        if (schema instanceof ArraySchema) {
            ArraySchema arSchema = (ArraySchema) schema;
            return new ArrayConstrainedValue(arSchema.getMaxItems(), arSchema.getMinItems(), arSchema.getUniqueItems());
        } else if (schema instanceof NumberSchema) {
            NumberSchema nuSchema = (NumberSchema) schema;
            return new NumberConstrainedValue(nuSchema.getMaximum(), nuSchema.getMinimum(), nuSchema.isExclusiveMaximum(), nuSchema.isExclusiveMinimum());
        } else if (schema instanceof StringSchema) {
            StringSchema stSchema = (StringSchema) schema;
            return new StringConstrainedValue(stSchema.getMaxLength(), stSchema.getMinLength());
        } else {
            return new NoConstrainedValue();
        }
    }

    private ConstrainedValue fromRequestParam(RequestParameter parameter) {
        if (parameter instanceof ArrayRequestParameter) {
            ArrayRequestParameter arParameter = (ArrayRequestParameter) parameter;
            return new ArrayConstrainedValue(arParameter.getMaxItems(), arParameter.getMinItems(), arParameter.getUniqueItems());
        } else if (parameter instanceof NumberRequestParameter) {
            NumberRequestParameter nuParameter = (NumberRequestParameter) parameter;
            return new NumberConstrainedValue(nuParameter.getMaximum(), nuParameter.getMinimum(), nuParameter.isExclusiveMaximum(), nuParameter.isExclusiveMinimum());
        } else if (parameter instanceof StringRequestParameter) {
            StringRequestParameter stParameter = (StringRequestParameter) parameter;
            return new StringConstrainedValue(stParameter.getMaxLength(), stParameter.getMinLength());
        } else {
            return new NoConstrainedValue();
        }
    }

    private <T extends ConstrainedValue> Collection<RequestParameterConstraintChangedBreakingChange> applyConstraints(Path path, T oldValue, T newValue, String attributeName) {
        Class<T> classType = (Class<T>) oldValue.getClass();
        Class<T> newClassType = (Class<T>) newValue.getClass();
        List<RequestParameterConstraintChangedBreakingChange> bcs = constraints.stream()
            .filter(c -> c.handledRequestParameter().equals(classType))
            .filter(c -> c.handledRequestParameter().equals(newClassType))
            .map(c -> ((Constraint<T>) c).validateConstraints(oldValue, newValue))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(cc -> new RequestParameterConstraintChangedBreakingChange(path.getPath(), path.getMethod(), attributeName, cc))
            .collect(Collectors.toList());
        return bcs;
    }
}
