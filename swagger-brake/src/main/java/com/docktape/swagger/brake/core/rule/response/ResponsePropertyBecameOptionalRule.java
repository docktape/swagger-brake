package com.docktape.swagger.brake.core.rule.response;

import java.util.*;

import com.docktape.swagger.brake.core.model.*;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponsePropertyBecameOptionalRule implements BreakingChangeRule<ResponsePropertyBecameOptionalBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponsePropertyBecameOptionalBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponsePropertyBecameOptionalBreakingChange> breakingChanges = new HashSet<>();
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
                            Schema schema = entry.getValue();
                            Optional<Schema> newApiSchema = newResponse.getSchemaByMediaType(mediaType);
                            if (newApiSchema.isPresent()) {
                                Schema newSchema = newApiSchema.get();
                                Set<String> oldRequiredAttributes = schema.getRequiredAttributeNames();
                                Set<String> newRequiredAttributes = newSchema.getRequiredAttributeNames();
                                for (String oldRequired : oldRequiredAttributes) {
                                    if (!newRequiredAttributes.contains(oldRequired)) {
                                        breakingChanges.add(
                                                new ResponsePropertyBecameOptionalBreakingChange(
                                                        path.getPath(), path.getMethod(), apiResponse.getCode(), oldRequired));
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
