package com.docktape.swagger.brake.core.rule.response;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Response;
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
public class ResponsePropertyConstChangedRule implements BreakingChangeRule<ResponsePropertyConstChangedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponsePropertyConstChangedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponsePropertyConstChangedBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                for (Response apiResponse : path.getResponses()) {
                    Optional<Response> newApiResponse = newPath.getResponseByCode(apiResponse.getCode());
                    if (newApiResponse.isPresent()) {
                        Response newResponse = newApiResponse.get();
                        for (Map.Entry<MediaType, Schema> entry : apiResponse.getMediaTypes().entrySet()) {
                            MediaType mediaType = entry.getKey();
                            Schema oldSchema = entry.getValue();
                            Optional<Schema> newApiSchema = newResponse.getSchemaByMediaType(mediaType);
                            if (newApiSchema.isPresent()) {
                                Schema newSchema = newApiSchema.get();
                                Map<String, Schema> oldSchemas = oldSchema.getSchemasRecursively();
                                Map<String, Schema> newSchemas = newSchema.getSchemasRecursively();
                                for (Map.Entry<String, Schema> schemaEntry : oldSchemas.entrySet()) {
                                    String propertyName = schemaEntry.getKey();
                                    String oldConst = schemaEntry.getValue().getConstValue();
                                    if (oldConst == null) {
                                        continue;
                                    }
                                    Schema newPropertySchema = newSchemas.get(propertyName);
                                    if (newPropertySchema == null) {
                                        continue;
                                    }
                                    String newConst = newPropertySchema.getConstValue();
                                    if (!oldConst.equals(newConst)) {
                                        breakingChanges.add(new ResponsePropertyConstChangedBreakingChange(
                                            path.getPath(), path.getMethod(), apiResponse.getCode(), propertyName));
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
}
