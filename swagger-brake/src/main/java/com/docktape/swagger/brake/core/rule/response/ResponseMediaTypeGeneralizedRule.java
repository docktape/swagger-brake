package com.docktape.swagger.brake.core.rule.response;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Response;
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
public class ResponseMediaTypeGeneralizedRule implements BreakingChangeRule<ResponseMediaTypeGeneralizedBreakingChange> {
    private final PathSkipper pathSkipper;

    @Override
    public Collection<ResponseMediaTypeGeneralizedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        Set<ResponseMediaTypeGeneralizedBreakingChange> breakingChanges = new HashSet<>();
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
                    if (newApiResponse.isPresent()) {
                        Response newResponse = newApiResponse.get();
                        checkMediaTypeGeneralized(breakingChanges, path, apiResponse, newResponse);
                    }
                }
            }
        }
        return breakingChanges;
    }

    private void checkMediaTypeGeneralized(Set<ResponseMediaTypeGeneralizedBreakingChange> breakingChanges,
            Path path, Response oldResponse, Response newResponse) {
        Set<MediaType> newMediaTypes = newResponse.getMediaTypes().keySet();
        for (Map.Entry<MediaType, Schema> entry : oldResponse.getMediaTypes().entrySet()) {
            String oldMime = entry.getKey().getMimeType();
            if (newResponse.isMediaTypeAllowed(entry.getKey())) {
                continue;
            }
            for (MediaType newMediaType : newMediaTypes) {
                String newMime = newMediaType.getMimeType();
                if (isGeneralization(oldMime, newMime)) {
                    breakingChanges.add(new ResponseMediaTypeGeneralizedBreakingChange(
                        path.getPath(), path.getMethod(), oldResponse.getCode(), oldMime, newMime));
                    break;
                }
            }
        }
    }

    private boolean isGeneralization(String oldMime, String newMime) {
        if (oldMime.endsWith("+json") && "application/json".equals(newMime)) {
            return true;
        }
        if (oldMime.endsWith("+xml") && "application/xml".equals(newMime)) {
            return true;
        }
        if (oldMime.startsWith("application/vnd.")) {
            if (newMime.startsWith("application/") && newMime.equals("application/*")) {
                return true;
            }
            if ("*/*".equals(newMime)) {
                return true;
            }
        }
        return false;
    }
}
