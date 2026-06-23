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
public class RequestAdditionalPropertiesTightenedRule
        implements BreakingChangeRule<RequestAdditionalPropertiesTightenedBreakingChange> {

    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestAdditionalPropertiesTightenedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestAdditionalPropertiesTightenedBreakingChange> breakingChanges = new HashSet<>();
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
                            checkSchemasRecursively(path.getPath(), path.getMethod(), oldSchema, newSchema, breakingChanges);
                        }
                    }
                }
            }
        }
        return breakingChanges;
    }

    private void checkSchemasRecursively(String pathStr, com.docktape.swagger.brake.core.model.HttpMethod method,
                                         Schema oldSchema, Schema newSchema,
                                         Set<RequestAdditionalPropertiesTightenedBreakingChange> breakingChanges) {
        Map<String, Schema> oldSchemas = oldSchema.getSchemasRecursively();
        Map<String, Schema> newSchemas = newSchema.getSchemasRecursively();
        for (Map.Entry<String, Schema> entry : oldSchemas.entrySet()) {
            String schemaPath = entry.getKey();
            Schema oldSubSchema = entry.getValue();
            Schema newSubSchema = newSchemas.get(schemaPath);
            if (newSubSchema == null) {
                continue;
            }
            Boolean oldAllowed = oldSubSchema.getAdditionalPropertiesAllowed();
            Boolean newAllowed = newSubSchema.getAdditionalPropertiesAllowed();
            // Breaking if old was null (unspecified, treated as allowed) or true, and new is false
            boolean oldWasAllowed = !Boolean.FALSE.equals(oldAllowed);
            boolean newIsDisallowed = Boolean.FALSE.equals(newAllowed);
            if (oldWasAllowed && newIsDisallowed) {
                breakingChanges.add(new RequestAdditionalPropertiesTightenedBreakingChange(pathStr, method, schemaPath));
            }
        }
    }
}
