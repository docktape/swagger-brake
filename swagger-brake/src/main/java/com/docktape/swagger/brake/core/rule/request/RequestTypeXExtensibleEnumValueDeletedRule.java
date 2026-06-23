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
public class RequestTypeXExtensibleEnumValueDeletedRule
    implements BreakingChangeRule<RequestTypeXExtensibleEnumValueDeletedBreakingChange> {

    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestTypeXExtensibleEnumValueDeletedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestTypeXExtensibleEnumValueDeletedBreakingChange> breakingChanges = new HashSet<>();
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
                            Map<String, Set<String>> oldXExtEnums = schema.getXExtensibleEnums();
                            Map<String, Set<String>> newXExtEnums = newApiSchema.get().getXExtensibleEnums();
                            for (Map.Entry<String, Set<String>> entry2 : oldXExtEnums.entrySet()) {
                                String attributeName = entry2.getKey();
                                Set<String> oldValues = entry2.getValue();
                                Set<String> newValues = newXExtEnums.getOrDefault(attributeName, Set.of());
                                for (String oldValue : oldValues) {
                                    if (!newValues.contains(oldValue)) {
                                        breakingChanges.add(
                                            new RequestTypeXExtensibleEnumValueDeletedBreakingChange(
                                                path.getPath(), path.getMethod(), attributeName, oldValue));
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
