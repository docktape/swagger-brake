package com.docktape.swagger.brake.core.rule.request;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Request;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestBodyBecameRequiredRule implements BreakingChangeRule<RequestBodyBecameRequiredBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestBodyBecameRequiredBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestBodyBecameRequiredBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                Optional<Request> oldRequestBody = path.getRequestBody();
                Optional<Request> newRequestBody = newPath.getRequestBody();
                if (newRequestBody.isPresent() && newRequestBody.get().isRequired()) {
                    boolean oldWasNotRequired = !oldRequestBody.isPresent() || !oldRequestBody.get().isRequired();
                    if (oldWasNotRequired) {
                        breakingChanges.add(new RequestBodyBecameRequiredBreakingChange(path.getPath(), path.getMethod()));
                    }
                }
            }
        }
        return breakingChanges;
    }
}
