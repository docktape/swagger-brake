package com.docktape.swagger.brake.core.rule.response;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Response;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseDeletedRule implements BreakingChangeRule<ResponseDeletedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponseDeletedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponseDeletedBreakingChange> breakingChanges = new HashSet<>();
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
                    if (!newApiResponse.isPresent()) {
                        breakingChanges.add(new ResponseDeletedBreakingChange(path.getPath(), path.getMethod(), apiResponse.getCode()));
                    }
                }
            }
        }
        return breakingChanges;
    }
}
