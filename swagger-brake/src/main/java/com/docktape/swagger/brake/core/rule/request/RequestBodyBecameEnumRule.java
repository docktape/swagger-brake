package com.docktape.swagger.brake.core.rule.request;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Request;
import com.docktape.swagger.brake.core.model.Schema;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.model.parameter.RequestParameter;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestBodyBecameEnumRule implements BreakingChangeRule<RequestBodyBecameEnumBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestBodyBecameEnumBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestBodyBecameEnumBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                checkRequestBody(path, newPath, breakingChanges);
                checkRequestParameters(path, newPath, breakingChanges);
            }
        }
        return breakingChanges;
    }

    private void checkRequestBody(Path path, Path newPath, Set<RequestBodyBecameEnumBreakingChange> breakingChanges) {
        Optional<Request> requestBody = path.getRequestBody();
        Optional<Request> newRequestBody = newPath.getRequestBody();
        if (!requestBody.isPresent() || !newRequestBody.isPresent()) {
            return;
        }
        for (Map.Entry<MediaType, Schema> entry : requestBody.get().getMediaTypes().entrySet()) {
            MediaType mediaType = entry.getKey();
            Schema oldSchema = entry.getValue();
            Optional<Schema> newApiSchema = newRequestBody.get().getSchemaByMediaType(mediaType);
            if (!newApiSchema.isPresent()) {
                continue;
            }
            Schema newSchema = newApiSchema.get();
            Map<String, Schema> oldSchemas = oldSchema.getSchemasRecursively();
            Map<String, Schema> newSchemas = newSchema.getSchemasRecursively();
            for (Map.Entry<String, Schema> schemaEntry : oldSchemas.entrySet()) {
                String propertyName = schemaEntry.getKey();
                Schema oldPropertySchema = schemaEntry.getValue();
                Schema newPropertySchema = newSchemas.get(propertyName);
                if (newPropertySchema == null) {
                    continue;
                }
                if (CollectionUtils.isEmpty(oldPropertySchema.getEnumValues())
                        && CollectionUtils.isNotEmpty(newPropertySchema.getEnumValues())) {
                    breakingChanges.add(
                        new RequestBodyBecameEnumBreakingChange(path.getPath(), path.getMethod(), propertyName));
                }
            }
        }
    }

    private void checkRequestParameters(Path path, Path newPath, Set<RequestBodyBecameEnumBreakingChange> breakingChanges) {
        if (CollectionUtils.isEmpty(path.getRequestParameters())) {
            return;
        }
        for (RequestParameter requestParameter : path.getRequestParameters()) {
            Optional<RequestParameter> newRequestParameter = newPath.getRequestParameterByName(requestParameter.getName());
            if (!newRequestParameter.isPresent()) {
                continue;
            }
            Optional<Schema> oldSchema = requestParameter.getSchema();
            Optional<Schema> newSchema = newRequestParameter.get().getSchema();
            if (!oldSchema.isPresent() || !newSchema.isPresent()) {
                continue;
            }
            if (CollectionUtils.isEmpty(oldSchema.get().getEnumValues())
                    && CollectionUtils.isNotEmpty(newSchema.get().getEnumValues())) {
                breakingChanges.add(
                    new RequestBodyBecameEnumBreakingChange(path.getPath(), path.getMethod(), requestParameter.getName()));
            }
        }
    }
}
