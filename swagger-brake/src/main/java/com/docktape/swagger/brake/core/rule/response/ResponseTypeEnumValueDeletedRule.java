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
public class ResponseTypeEnumValueDeletedRule implements BreakingChangeRule<ResponseTypeEnumValueDeletedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponseTypeEnumValueDeletedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponseTypeEnumValueDeletedBreakingChange> breakingChanges = new HashSet<>();
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
                                Collection<String> oldEnumValues = schema.getEnums();
                                Collection<String> newEnumValues = newApiSchema.get().getEnums();
                                for (String oldEnumValue : oldEnumValues) {
                                    if (!newEnumValues.contains(oldEnumValue)) {
                                        breakingChanges.add(
                                            new ResponseTypeEnumValueDeletedBreakingChange(path.getPath(), path.getMethod(), oldEnumValue));
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
