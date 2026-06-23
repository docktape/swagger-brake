package com.docktape.swagger.brake.core.rule.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServerUrlChangedRule implements BreakingChangeRule<ServerUrlChangedBreakingChange> {
    private final CheckerOptionsProvider checkerOptionsProvider;

    @Override
    public Collection<ServerUrlChangedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        log.debug("Checking for server URL changes..");
        CheckerOptions checkerOptions = checkerOptionsProvider.get();
        if (!checkerOptions.isServerUrlChangeEnabled()) {
            log.debug("Server URL change check is disabled, skipping");
            return new ArrayList<>();
        }
        List<String> oldUrls = oldApi.getServerUrls();
        List<String> newUrls = newApi.getServerUrls();
        Collection<ServerUrlChangedBreakingChange> breakingChanges = new ArrayList<>();
        for (String oldUrl : oldUrls) {
            if (!newUrls.contains(oldUrl)) {
                log.debug("Server URL {} is not present in the new API", oldUrl);
                breakingChanges.add(new ServerUrlChangedBreakingChange(oldUrl));
            }
        }
        log.debug("Found server URL changes: {}", breakingChanges);
        return breakingChanges;
    }
}
