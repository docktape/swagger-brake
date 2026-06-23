package com.docktape.swagger.brake.report;

import com.docktape.swagger.brake.core.ApiInfo;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
class GitHubActionsReporter implements Reporter, CheckableReporter {
    @Override
    public void report(Collection<BreakingChange> breakingChanges, Collection<BreakingChange> ignoredBreakingChanges, Options options, ApiInfo apiInfo) {
        breakingChanges.forEach(bc ->
            System.out.println("::error title=Breaking Change (" + bc.getRuleCode() + ")::" + bc.getMessage())
        );
    }

    @Override
    public boolean canReport(OutputFormat format) {
        return OutputFormat.GITHUB_ACTIONS.equals(format);
    }
}
