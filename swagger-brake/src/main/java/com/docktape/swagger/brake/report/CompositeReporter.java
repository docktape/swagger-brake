package com.docktape.swagger.brake.report;

import com.docktape.swagger.brake.core.ApiInfo;
import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.runner.Options;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CompositeReporter implements Reporter {
    private final Collection<Reporter> delegates;

    @Override
    public void report(Collection<BreakingChange> breakingChanges, Options options) {
        delegates.forEach(d -> d.report(breakingChanges, options));
    }

    @Override
    public void report(Collection<BreakingChange> breakingChanges, Options options, ApiInfo apiInfo) {
        delegates.forEach(d -> d.report(breakingChanges, options, apiInfo));
    }

    @Override
    public void report(Collection<BreakingChange> breakingChanges, Collection<BreakingChange> ignoredBreakingChanges, Options options, ApiInfo apiInfo) {
        delegates.forEach(d -> d.report(breakingChanges, ignoredBreakingChanges, options, apiInfo));
    }
}
