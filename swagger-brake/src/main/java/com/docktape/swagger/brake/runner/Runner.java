package com.docktape.swagger.brake.runner;

import com.docktape.swagger.brake.core.ApiInfo;
import com.docktape.swagger.brake.runner.openapi.ApiInfoFactory;
import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.report.ReporterFactory;
import com.docktape.swagger.brake.runner.download.ArtifactDownloaderHandler;
import com.docktape.swagger.brake.runner.openapi.OpenApiFactory;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * The main entrypoint of Swagger Brake that can be used programmatically.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class Runner {
    private final OptionsValidator optionsValidator;
    private final ArtifactDownloaderHandler artifactDownloaderHandler;
    private final OpenApiFactory openApiFactory;
    private final CheckerOptionsFactory checkerOptionsFactory;
    private final Checker checker;
    private final ReporterFactory reporterFactory;
    private final ApiInfoFactory apiInfoFactory;

    /**
     * Runs Swagger Brake with the specified {@link Options}.
     * @param options the options that should be used for the execution.
     * @return a collection of breaking changes or an empty collection if no breaking change has been detected.
     */
    public Collection<BreakingChange> run(Options options) {
        optionsValidator.validate(options);
        artifactDownloaderHandler.handle(options);
        String oldApiPath = options.getOldApiPath();
        if (StringUtils.isBlank(oldApiPath)) {
            throw new IllegalArgumentException("oldApiPath must be provided");
        }
        String newApiPath = options.getNewApiPath();
        if (StringUtils.isBlank(newApiPath)) {
            throw new IllegalArgumentException("newApiPath must be provided");
        }
        log.info("Loading old API from {}", oldApiPath);
        log.info("Loading new API from {}", newApiPath);
        OpenAPI oldApi = openApiFactory.fromFile(oldApiPath);
        OpenAPI newApi = openApiFactory.fromFile(newApiPath);
        log.info("Successfully loaded APIs");
        CheckerOptions checkerOptions = checkerOptionsFactory.create(options);
        Collection<BreakingChange> allBreakingChanges = checker.check(oldApi, newApi, checkerOptions);
        Collection<BreakingChange> breakingChanges = allBreakingChanges.stream().filter(bc -> isNotIgnoredBreakingChange(options, bc)).toList();
        Collection<BreakingChange> ignoredBreakingChanges = allBreakingChanges.stream().filter(bc -> isIgnoredBreakingChange(options, bc)).toList();

        ApiInfo apiInfo = apiInfoFactory.create(newApi);
        reporterFactory.create(options).report(breakingChanges, options, apiInfo);
        return breakingChanges;
    }

    private boolean isNotIgnoredBreakingChange(Options options, BreakingChange breakingChange) {
        return !isIgnoredBreakingChange(options, breakingChange);
    }

    private boolean isIgnoredBreakingChange(Options options, BreakingChange breakingChange) {
        return options.getIgnoredBreakingChangeRules().contains(breakingChange.getRuleCode());
    }
}
