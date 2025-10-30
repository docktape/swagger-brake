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
public class ResponseTypeAttributeRemovedRule implements BreakingChangeRule<ResponseTypeAttributeRemovedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponseTypeAttributeRemovedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponseTypeAttributeRemovedBreakingChange> breakingChanges = new HashSet<>();
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
                                Collection<String> oldAttributeNames = schema.getNonDeprecatedAttributeNames();
                                Collection<String> newAttributeNames = newSchema.getAttributeNames();
                                for (String oldAttributeName : oldAttributeNames) {
                                    if (!newAttributeNames.contains(oldAttributeName)) {
                                        breakingChanges.add(
                                                new ResponseTypeAttributeRemovedBreakingChange(path.getPath(), path.getMethod(), apiResponse.getCode(), oldAttributeName));
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
