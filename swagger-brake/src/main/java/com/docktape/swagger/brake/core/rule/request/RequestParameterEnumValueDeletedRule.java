package com.docktape.swagger.brake.core.rule.request;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Schema;
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
public class RequestParameterEnumValueDeletedRule  implements BreakingChangeRule<RequestParameterEnumValueDeletedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestParameterEnumValueDeletedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestParameterEnumValueDeletedBreakingChange> breakingChanges = new HashSet<>();
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
                            Optional<Schema> schema = requestParameter.getSchema();
                            Optional<Schema> newSchema = newRequestParam.getSchema();
                            if (schema.isPresent() && newSchema.isPresent()) {
                                Collection<String> oldEnumValues = schema.get().getEnums();
                                Collection<String> newEnumValues = newSchema.get().getEnums();
                                for (String oldEnumValue : oldEnumValues) {
                                    if (!newEnumValues.contains(oldEnumValue)) {
                                        breakingChanges.add(
                                            new RequestParameterEnumValueDeletedBreakingChange(path.getPath(), path.getMethod(),
                                                requestParameter.getName(), oldEnumValue));
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
