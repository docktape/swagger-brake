package com.docktape.swagger.brake.core.rule.response;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.docktape.swagger.brake.core.model.ArraySchema;
import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.model.NumberSchema;
import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Response;
import com.docktape.swagger.brake.core.model.Schema;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.model.StringSchema;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ArrayConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.Constraint;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.NoConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.NumberConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.StringConstrainedValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseConstraintChangedRule implements BreakingChangeRule<ResponseConstraintChangedBreakingChange> {
    private final Collection<Constraint<?>> constraints;
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponseConstraintChangedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponseConstraintChangedBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                for (Response oldResponse : path.getResponses()) {
                    Optional<Response> newApiResponse = newPath.getResponseByCode(oldResponse.getCode());
                    if (newApiResponse.isPresent()) {
                        Response newResponse = newApiResponse.get();
                        for (Map.Entry<MediaType, Schema> entry : oldResponse.getMediaTypes().entrySet()) {
                            MediaType mediaType = entry.getKey();
                            Schema oldSchema = entry.getValue();
                            Optional<Schema> newApiSchema = newResponse.getSchemaByMediaType(mediaType);
                            if (newApiSchema.isPresent()) {
                                Schema newSchema = newApiSchema.get();
                                Map<String, Schema> oldSchemas = oldSchema.getSchemasRecursively();
                                Map<String, Schema> newSchemas = newSchema.getSchemasRecursively();
                                for (Map.Entry<String, Schema> schemaEntry : oldSchemas.entrySet()) {
                                    Schema newSubSchema = newSchemas.get(schemaEntry.getKey());
                                    if (newSubSchema != null) {
                                        breakingChanges.addAll(applyConstraints(path, oldResponse.getCode(),
                                            fromSchema(schemaEntry.getValue()), fromSchema(newSubSchema), schemaEntry.getKey()));
                                    }
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
            return new NumberConstrainedValue(nuSchema.getMaximum(), nuSchema.getMinimum(), nuSchema.isExclusiveMaximum(), nuSchema.isExclusiveMinimum(), nuSchema.getMultipleOf());
        } else if (schema instanceof StringSchema) {
            StringSchema stSchema = (StringSchema) schema;
            return new StringConstrainedValue(stSchema.getMaxLength(), stSchema.getMinLength());
        } else {
            return new NoConstrainedValue();
        }
    }

    private <T extends ConstrainedValue> Collection<ResponseConstraintChangedBreakingChange> applyConstraints(
            Path path, String responseCode, T oldValue, T newValue, String attributeName) {
        Class<T> classType = (Class<T>) oldValue.getClass();
        Class<T> newClassType = (Class<T>) newValue.getClass();
        // Swap old/new so that loosening (new has larger max or smaller min) looks like tightening to the constraint impls
        List<ResponseConstraintChangedBreakingChange> bcs = constraints.stream()
            .filter(c -> c.handledRequestParameter().equals(classType))
            .filter(c -> c.handledRequestParameter().equals(newClassType))
            .map(c -> ((Constraint<T>) c).validateConstraints(newValue, oldValue))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(cc -> new ResponseConstraintChangedBreakingChange(path.getPath(), path.getMethod(), responseCode, attributeName, cc))
            .collect(Collectors.toList());
        return bcs;
    }
}
