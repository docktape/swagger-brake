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
public class RequestPropertyBecameNonNullableRule implements BreakingChangeRule<RequestPropertyBecameNonNullableBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestPropertyBecameNonNullableBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestPropertyBecameNonNullableBreakingChange> breakingChanges = new HashSet<>();
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
                            Schema newSchema = newApiSchema.get();
                            Map<String, Boolean> oldNullable = schema.getNullableAttributes();
                            Map<String, Boolean> newNullable = newSchema.getNullableAttributes();
                            for (Map.Entry<String, Boolean> nullableEntry : oldNullable.entrySet()) {
                                String attrName = nullableEntry.getKey();
                                boolean wasNullable = Boolean.TRUE.equals(nullableEntry.getValue());
                                boolean isNullable = Boolean.TRUE.equals(newNullable.get(attrName));
                                if (wasNullable && !isNullable) {
                                    breakingChanges.add(
                                        new RequestPropertyBecameNonNullableBreakingChange(path.getPath(), path.getMethod(), attrName));
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
