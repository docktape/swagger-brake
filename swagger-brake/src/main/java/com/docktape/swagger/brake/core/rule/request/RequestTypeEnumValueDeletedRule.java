package com.docktape.swagger.brake.core.rule.request;

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
public class RequestTypeEnumValueDeletedRule implements BreakingChangeRule<RequestTypeEnumValueDeletedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestTypeEnumValueDeletedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestTypeEnumValueDeletedBreakingChange> breakingChanges = new HashSet<>();
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
                        Schema schema = entry.getValue();
                        Optional<Schema> newApiSchema = newRequestBody.get().getSchemaByMediaType(mediaType);
                        if (newApiSchema.isPresent()) {
                            Collection<String> oldEnumValues = schema.getEnums();
                            Collection<String> newEnumValues = newApiSchema.get().getEnums();
                            for (String oldEnumValue : oldEnumValues) {
                                if (!newEnumValues.contains(oldEnumValue)) {
                                    breakingChanges.add(
                                        new RequestTypeEnumValueDeletedBreakingChange(path.getPath(), path.getMethod(), oldEnumValue));
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
