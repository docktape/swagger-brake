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
public class RequestMediaTypeDeletedRule implements BreakingChangeRule<RequestMediaTypeDeletedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestMediaTypeDeletedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestMediaTypeDeletedBreakingChange> breakingChanges = new HashSet<>();
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
                    Request request = requestBody.get();
                    Request newRequest = newRequestBody.get();
                    checkMediaTypeBreaking(breakingChanges, path, request, newRequest);
                }
            }
        }
        return breakingChanges;
    }

    private void checkMediaTypeBreaking(Set<RequestMediaTypeDeletedBreakingChange> breakingChanges, Path path, Request request, Request newRequest) {
        for (Map.Entry<MediaType, Schema> entry : request.getMediaTypes().entrySet()) {
            MediaType mediaType = entry.getKey();
            if (!newRequest.isMediaTypeAllowed(mediaType)) {
                breakingChanges.add(new RequestMediaTypeDeletedBreakingChange(path.getPath(), path.getMethod(), mediaType));
            }
        }
    }
}
