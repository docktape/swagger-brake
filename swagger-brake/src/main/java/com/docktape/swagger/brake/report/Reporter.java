package com.docktape.swagger.brake.report;

import com.docktape.swagger.brake.core.ApiInfo;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.runner.Options;
import java.util.Collection;
import java.util.Collections;

public interface Reporter {
    default void report(Collection<BreakingChange> breakingChanges, Options options) {
        report(breakingChanges, options, null);
    }

    default void report(Collection<BreakingChange> breakingChanges, Options options, ApiInfo apiInfo) {
        report(breakingChanges, Collections.emptyList(), options, apiInfo);
    }

    void report(Collection<BreakingChange> breakingChanges, Collection<BreakingChange> ignoredBreakingChanges, Options options, ApiInfo apiInfo);
}
