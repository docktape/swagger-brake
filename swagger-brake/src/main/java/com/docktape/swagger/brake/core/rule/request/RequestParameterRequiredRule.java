package com.docktape.swagger.brake.core.rule.request;

import java.util.*;

import com.docktape.swagger.brake.core.model.*;
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
public class RequestParameterRequiredRule implements BreakingChangeRule<RequestParameterRequiredBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<RequestParameterRequiredBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<RequestParameterRequiredBreakingChange> breakingChanges = new HashSet<>();
        for (Path path : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(path)) {
                log.debug("Skipping {} as it's marked as a beta API", path);
                continue;
            }
            Optional<Path> newApiPath = newApi.getPath(path);
            if (newApiPath.isPresent()) {
                Path newPath = newApiPath.get();
                breakingChanges.addAll(checkQueryParameters(path, newPath));
                breakingChanges.addAll(checkRequestBody(path, newPath));
            }
        }
        return breakingChanges;
    }

    private Set<RequestParameterRequiredBreakingChange> checkRequestBody(Path path, Path newPath) {
        Set<RequestParameterRequiredBreakingChange> breakingChanges = new HashSet<>();
        if (path.getRequestBody().isPresent() && newPath.getRequestBody().isPresent()) {
            Request requestBody = path.getRequestBody().get();
            Request newRequestBody = newPath.getRequestBody().get();
            for (Map.Entry<MediaType, Schema> entry : requestBody.getMediaTypes().entrySet()) {
                MediaType mediaType = entry.getKey();
                Schema schema = entry.getValue();
                Optional<Schema> optionalNewSchema = newRequestBody.getSchemaByMediaType(mediaType);
                if (optionalNewSchema.isPresent()) {
                    Schema newSchema = optionalNewSchema.get();
                    Set<String> oldRequiredAttributeNames = schema.getRequiredAttributeNames();
                    Set<String> remainingRequiredAttributeNames = newSchema.getRequiredAttributeNames();
                    for (String oldRequiredAttributeName : oldRequiredAttributeNames) {
                        remainingRequiredAttributeNames.remove(oldRequiredAttributeName);
                    }
                    if (remainingRequiredAttributeNames.size() > 0) {
                        for (String remainingRequiredAttr : remainingRequiredAttributeNames) {
                            breakingChanges.add(new RequestParameterRequiredBreakingChange(path.getPath(), path.getMethod(), remainingRequiredAttr));
                        }
                    }
                }
            }
        }
        return breakingChanges;
    }

    private Set<RequestParameterRequiredBreakingChange> checkQueryParameters(Path path, Path newPath) {
        Set<RequestParameterRequiredBreakingChange> breakingChanges = new HashSet<>();
        if (CollectionUtils.isNotEmpty(path.getRequestParameters())) {
            breakingChanges.addAll(checkOldNonRequiredQueryParameters(path, newPath));
        }
        if (CollectionUtils.isNotEmpty(newPath.getRequestParameters())) {
            breakingChanges.addAll(checkNewRequiredQueryParameters(path, newPath));
        }
        return breakingChanges;
    }

    private Collection<RequestParameterRequiredBreakingChange> checkNewRequiredQueryParameters(Path path, Path newPath) {
        Set<RequestParameterRequiredBreakingChange> breakingChanges = new HashSet<>();
        for (RequestParameter newRequestParameter : newPath.getRequestParameters()) {
            Optional<RequestParameter> oldRequestParameter = path.getRequestParameterByName(newRequestParameter.getName());
            if (!oldRequestParameter.isPresent()) {
                if (newRequestParameter.isRequired()) {
                    breakingChanges.add(new RequestParameterRequiredBreakingChange(path.getPath(), path.getMethod(), newRequestParameter.getName()));
                }
            }
        }
        return breakingChanges;
    }

    private Collection<RequestParameterRequiredBreakingChange> checkOldNonRequiredQueryParameters(Path path, Path newPath) {
        Set<RequestParameterRequiredBreakingChange> breakingChanges = new HashSet<>();
        for (RequestParameter requestParameter : path.getRequestParameters()) {
            Optional<RequestParameter> newRequestParam = newPath.getRequestParameterByName(requestParameter.getName());
            if (newRequestParam.isPresent()) {
                RequestParameter newRequestParameter = newRequestParam.get();
                if (!requestParameter.isRequired() && newRequestParameter.isRequired()) {
                    breakingChanges.add(new RequestParameterRequiredBreakingChange(path.getPath(), path.getMethod(), requestParameter.getName()));
                }
            }
        }
        return breakingChanges;
    }
}
