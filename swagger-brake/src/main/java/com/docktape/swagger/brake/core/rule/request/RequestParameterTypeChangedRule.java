package com.docktape.swagger.brake.core.rule.request;

import java.util.*;

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
public class RequestParameterTypeChangedRule implements BreakingChangeRule<RequestParameterTypeChangedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestParameterTypeChangedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestParameterTypeChangedBreakingChange> breakingChanges = new HashSet<>();
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
                            if (requestParameter.getSchema().isPresent()) {
                                if (newRequestParam.getSchema().isPresent()) {
                                    Schema schema = requestParameter.getSchema().get();
                                    Schema newSchema = newRequestParam.getSchema().get();
                                    Map<String, String> newTypes = newSchema.getTypes();
                                    for (Map.Entry<String, String> type : schema.getTypes().entrySet()) {
                                        String attribute = type.getKey();
                                        String typeName = type.getValue();
                                        String newType = newTypes.get(attribute);
                                        if (newType != null && !newType.equals(typeName)) {
                                            breakingChanges.add(
                                                new RequestParameterTypeChangedBreakingChange(path.getPath(), path.getMethod(),
                                                    requestParameter.getName(), attribute, typeName, newType));
                                        }
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
