package com.docktape.swagger.brake.runner;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.docktape.swagger.brake.core.BreakChecker;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.model.transformer.Transformer;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersion;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersionContext;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

@Component
@Slf4j
@RequiredArgsConstructor
class Checker {
    private final Transformer<OpenAPI, Specification> transformer;
    private final BreakChecker breakChecker;
    private final CheckerOptionsProvider checkerOptionsProvider;

    public Collection<BreakingChange> check(OpenAPI oldApi, OpenAPI newApi, CheckerOptions checkerOptions) {
        log.info("Starting the check for breaking API changes with options: {}", checkerOptions);
        if (checkerOptions == null) {
            throw new IllegalArgumentException("checkerOptions must be provided");
        }
        checkerOptionsProvider.set(checkerOptions);

        // Extract versions from APIs (checks extensions first, then openapi field)
        OpenApiVersion oldVersion = OpenApiVersion.fromOpenApi(oldApi);
        OpenApiVersion newVersion = OpenApiVersion.fromOpenApi(newApi);
        log.info("Comparing APIs: old version={}, new version={}", oldVersion, newVersion);

        // Transform old API with its version context
        Mono<Specification> oldSpecMono = Mono.fromCallable(() -> {
            log.debug("Transforming old API with version context: {}", oldVersion);
            try (OpenApiVersionContext context = new OpenApiVersionContext(oldVersion)) {
                log.trace("Version context set for old API transformation: {}", OpenApiVersionContext.getCurrentVersion());
                return transformer.transform(oldApi);
            }
        }).subscribeOn(Schedulers.boundedElastic());

        // Transform new API with its version context
        Mono<Specification> newSpecMono = Mono.fromCallable(() -> {
            log.debug("Transforming new API with version context: {}", newVersion);
            try (OpenApiVersionContext context = new OpenApiVersionContext(newVersion)) {
                log.trace("Version context set for new API transformation: {}", OpenApiVersionContext.getCurrentVersion());
                return transformer.transform(newApi);
            }
        }).subscribeOn(Schedulers.boundedElastic());

        Tuple2<Specification, Specification> specs = Mono.zip(oldSpecMono, newSpecMono)
                .block();

        Specification oldApiSpec = specs.getT1();
        Specification newApiSpec = specs.getT2();
        Collection<BreakingChange> breakingChanges = breakChecker.check(oldApiSpec, newApiSpec);
        log.info("Check has finished. Found {}", breakingChanges);
        return breakingChanges;
    }
}
