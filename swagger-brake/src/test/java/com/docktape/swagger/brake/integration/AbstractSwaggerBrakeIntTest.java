package com.docktape.swagger.brake.integration;

import com.google.common.collect.ImmutableSet;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.CoreConfiguration;
import com.docktape.swagger.brake.maven.MavenConfiguration;
import com.docktape.swagger.brake.report.ReporterConfiguration;
import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import com.docktape.swagger.brake.runner.Runner;
import com.docktape.swagger.brake.runner.RunnerConfiguration;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {CoreConfiguration.class, RunnerConfiguration.class, ReporterConfiguration.class, MavenConfiguration.class})
public abstract class AbstractSwaggerBrakeIntTest {
    @Autowired
    protected Runner underTest;

    protected Collection<BreakingChange> execute(String oldApiPath, String newApiPath) {
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(true);
        return execute(options);
    }

    protected Collection<BreakingChange> execute(Options options) {
        options.setOutputFormats(ImmutableSet.of(OutputFormat.STDOUT));
        return underTest.run(options);
    }
}
