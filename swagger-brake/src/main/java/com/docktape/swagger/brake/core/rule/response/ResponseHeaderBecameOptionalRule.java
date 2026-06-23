package com.docktape.swagger.brake.core.rule.response;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Response;
import com.docktape.swagger.brake.core.model.ResponseHeader;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseHeaderBecameOptionalRule implements BreakingChangeRule<ResponseHeaderBecameOptionalBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponseHeaderBecameOptionalBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponseHeaderBecameOptionalBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                for (Response oldResponse : path.getResponses()) {
                    Optional<Response> newApiResponse = newPath.getResponseByCode(oldResponse.getCode());
                    if (newApiResponse.isPresent()) {
                        Response newResponse = newApiResponse.get();
                        checkHeadersBecameOptional(breakingChanges, path, oldResponse, newResponse);
                    }
                }
            }
        }
        return breakingChanges;
    }

    private void checkHeadersBecameOptional(Set<ResponseHeaderBecameOptionalBreakingChange> breakingChanges, Path path,
            Response oldResponse, Response newResponse) {
        for (ResponseHeader oldHeader : oldResponse.getHeaders().values()) {
            if (!oldHeader.isRequired()) {
                continue;
            }
            ResponseHeader newHeader = newResponse.getHeaders().get(oldHeader.getName());
            if (newHeader != null && !newHeader.isRequired()) {
                breakingChanges.add(new ResponseHeaderBecameOptionalBreakingChange(
                    path.getPath(), path.getMethod(), oldResponse.getCode(), oldHeader.getName()));
            }
        }
    }
}
