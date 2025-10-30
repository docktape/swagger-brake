package com.docktape.swagger.brake.core.rule.request;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.model.parameter.RequestParameter;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestParameterDeletedRule implements BreakingChangeRule<RequestParameterDeletedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestParameterDeletedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestParameterDeletedBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                if (CollectionUtils.isNotEmpty(path.getRequestParameters())) {
                    for (RequestParameter requestParameter : path.getRequestParameters()) {
                        Optional<RequestParameter> newRequestParameter = newPath.getRequestParameterByName(requestParameter.getName());
                        if (!newRequestParameter.isPresent()) {
                            breakingChanges.add(
                                new RequestParameterDeletedBreakingChange(path.getPath(), path.getMethod(), requestParameter.getName()));
                        }
                    }
                }
            }
        }
        return breakingChanges;
    }
}
