package com.docktape.swagger.brake.report;

import com.docktape.swagger.brake.core.ApiInfo;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class StdOutReporter implements Reporter, CheckableReporter {
    @Override
    public void report(Collection<BreakingChange> breakingChanges, Collection<BreakingChange> ignoredBreakingChanges, Options options, ApiInfo apiInfo) {
        printBreakingChangesIfAny(breakingChanges);
        printIgnoredBreakingChangesIfAny(ignoredBreakingChanges);
    }

    private void printBreakingChangesIfAny(Collection<BreakingChange> breakingChanges) {
        if (!breakingChanges.isEmpty()) {
            System.err.println("There were breaking API changes");
            breakingChanges.stream().map(bc -> bc.getRuleCode() + " " + bc.getMessage()).forEach(System.err::println);
        } else {
            System.out.println("No breaking API changes detected");
        }
    }

    private void printIgnoredBreakingChangesIfAny(Collection<BreakingChange> ignoredBreakingChanges) {
        if (!ignoredBreakingChanges.isEmpty()) {
            System.out.println("There were ignored breaking API changes");
            ignoredBreakingChanges.stream().map(bc -> bc.getRuleCode() + " " + bc.getMessage()).forEach(System.out::println);
        }
    }

    @Override
    public boolean canReport(OutputFormat format) {
        return true;
    }
}
