package com.docktape.swagger.brake.core.rule.path;

import java.util.ArrayList;
import java.util.Collection;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PathDeletedRule implements BreakingChangeRule<PathDeletedBreakingChange> {
    private final PathSkipper pathSkipper;
    private final CheckerOptionsProvider checkerOptionsProvider;

    @Override
    public Collection<PathDeletedBreakingChange> checkRule(Specification oldApi, Specification newApi) {
        log.debug("Checking for path deletions..");
        CheckerOptions checkerOptions = checkerOptionsProvider.get();
        Collection<PathDeletedBreakingChange> breakingChanges = new ArrayList<>();
        for (Path p : oldApi.getPaths()) {
            if (pathSkipper.shouldSkip(p)) {
                log.debug("Skipping {} as it's marked as a beta API", p);
                continue;
            }
            if (!newApi.getPath(p).isPresent()) {
                if (!p.isDeprecated() || !checkerOptions.isDeprecatedApiDeletionAllowed()) {
                    log.debug("Path {} is not included in the new API", p);
                    breakingChanges.add(new PathDeletedBreakingChange(p.getPath(), p.getMethod()));
                } else {
                    log.debug("Path {} is not included in the new API however it was marked as deprecated", p);
                }
            } else {
                log.debug("Path {} is present in the new API as well", p);
            }
        }
        log.debug("Found path deletions: {}", breakingChanges);
        return breakingChanges;
    }
}
