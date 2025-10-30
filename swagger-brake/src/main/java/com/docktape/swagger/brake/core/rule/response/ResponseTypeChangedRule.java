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
public class ResponseTypeChangedRule implements BreakingChangeRule<ResponseTypeChangedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponseTypeChangedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponseTypeChangedBreakingChange> breakingChanges = new HashSet<>();
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
                                Map<String, String> newTypes = newSchema.getTypes();
                                for (Map.Entry<String, String> type : schema.getTypes().entrySet()) {
                                    String attribute = type.getKey();
                                    String typeName = type.getValue();
                                    String newType = newTypes.get(attribute);
                                    if (newType != null && !newType.equals(typeName)) {
                                        breakingChanges.add(
                                            new ResponseTypeChangedBreakingChange(path.getPath(), path.getMethod(), apiResponse.getCode(), attribute, typeName, newType));
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
