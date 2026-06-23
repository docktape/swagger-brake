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
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestPropertyConstChangedRule implements BreakingChangeRule<RequestPropertyConstChangedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestPropertyConstChangedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestPropertyConstChangedBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                Optional<Request> requestBody = path.getRequestBody();
                Optional<Request> newRequestBody = newPath.getRequestBody();
                if (requestBody.isPresent() && newRequestBody.isPresent()) {
                    for (Map.Entry<MediaType, Schema> entry : requestBody.get().getMediaTypes().entrySet()) {
                        MediaType mediaType = entry.getKey();
                        Schema oldSchema = entry.getValue();
                        Optional<Schema> newApiSchema = newRequestBody.get().getSchemaByMediaType(mediaType);
                        if (newApiSchema.isPresent()) {
                            Schema newSchema = newApiSchema.get();
                            Map<String, Schema> oldSchemas = oldSchema.getSchemasRecursively();
                            Map<String, Schema> newSchemas = newSchema.getSchemasRecursively();
                            for (Map.Entry<String, Schema> schemaEntry : oldSchemas.entrySet()) {
                                String propertyName = schemaEntry.getKey();
                                String oldConst = schemaEntry.getValue().getConstValue();
                                Schema newPropertySchema = newSchemas.get(propertyName);
                                if (newPropertySchema == null) {
                                    continue;
                                }
                                String newConst = newPropertySchema.getConstValue();
                                if (isConstBreaking(oldConst, newConst)) {
                                    breakingChanges.add(new RequestPropertyConstChangedBreakingChange(
                                        path.getPath(), path.getMethod(), propertyName, oldConst, newConst));
                                }
                            }
                        }
                    }
                }
            }
        }
        return breakingChanges;
    }

    private boolean isConstBreaking(String oldConst, String newConst) {
        if (oldConst == null && newConst != null) {
            // const added -> breaking (narrows to a single allowed value)
            return true;
        }
        if (oldConst != null && newConst != null && !oldConst.equals(newConst)) {
            // const changed -> breaking
            return true;
        }
        return false;
    }
}
