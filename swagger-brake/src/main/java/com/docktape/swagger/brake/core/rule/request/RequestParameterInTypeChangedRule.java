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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestParameterInTypeChangedRule implements BreakingChangeRule<RequestParameterInTypeChangedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestParameterInTypeChangedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestParameterInTypeChangedBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                if (CollectionUtils.isNotEmpty(path.getRequestParameters())) {
                    for (RequestParameter requestParameter : path.getRequestParameters()) {
                        Optional<RequestParameter> newRequestParameter = newPath.getRequestParameterByName(requestParameter.getName());
                        if (newRequestParameter.isPresent()) {
                            RequestParameter newRequestParam = newRequestParameter.get();
                            if (!requestParameter.getInType().equals(newRequestParam.getInType())) {
                                breakingChanges.add(
                                    new RequestParameterInTypeChangedBreakingChange(path.getPath(), path.getMethod(),
                                        requestParameter.getName(), requestParameter.getInType().getName(), newRequestParam.getInType().getName()));
                            }
                        }
                    }
                }
            }
        }
        return breakingChanges;
    }
}
