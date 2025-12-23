package com.docktape.swagger.brake.runner;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CoreConfiguration;
import com.docktape.swagger.brake.maven.MavenConfiguration;
import com.docktape.swagger.brake.report.ReporterConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Starter {
    public static Collection<BreakingChange> start(Options options) {
        log.info("Starting the application with options: {}", options);
        return createApplicationContext().getBean(Runner.class).run(options);
    }

    /**
     * Checks breaking changes between two {@link OpenAPI} instances.
     * <br>
     * For library users who want to use the check functionality only.
     * @param oldApi the old API
     * @param newApi the new API
     * @return a collection of breaking changes. The collection is never null.
     */
    public static Collection<BreakingChange> check(OpenAPI oldApi, OpenAPI newApi) {
        log.debug("Checking breaking changes between two OpenAPI instances with default options");
        return check(oldApi, newApi, new CheckerOptions());
    }

    /**
     * Checks breaking changes between two {@link OpenAPI} instances. The check configuration also can be provided
     * using this method.
     * <br>
     * For library users who want to use the check functionality only.
     * @param oldApi the old API
     * @param newApi the new API
     * @param checkerOptions the options for the check
     * @return a collection of breaking changes. The collection is never null.
     */
    public static Collection<BreakingChange> check(OpenAPI oldApi, OpenAPI newApi, CheckerOptions checkerOptions) {
        log.debug("Checking breaking changes between two OpenAPI instances with explicit options: {}", checkerOptions);
        return createApplicationContext().getBean(Checker.class).check(oldApi, newApi, checkerOptions);
    }

    private static AnnotationConfigApplicationContext createApplicationContext() {
        log.debug("Creating application context with configurations: RunnerConfiguration, ReporterConfiguration, MavenConfiguration, CoreConfiguration");
        return new AnnotationConfigApplicationContext(RunnerConfiguration.class, ReporterConfiguration.class,
                MavenConfiguration.class, CoreConfiguration.class);
    }
}
