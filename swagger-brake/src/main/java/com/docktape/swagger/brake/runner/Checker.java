package com.docktape.swagger.brake.runner;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakChecker;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.model.transformer.Transformer;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
        log.info("Starting the check for breaking API changes");
        if (checkerOptions == null) {
            throw new IllegalArgumentException("checkerOptions must be provided");
        }
        checkerOptionsProvider.set(checkerOptions);

        Mono<Specification> oldSpecMono = Mono.fromCallable(() -> transformer.transform(oldApi))
                .subscribeOn(Schedulers.boundedElastic());

        Mono<Specification> newSpecMono = Mono.fromCallable(() -> transformer.transform(newApi))
                .subscribeOn(Schedulers.boundedElastic());

        Tuple2<Specification, Specification> specs = Mono.zip(oldSpecMono, newSpecMono)
                .block();

        Specification oldApiSpec = specs.getT1();
        Specification newApiSpec = specs.getT2();
        Collection<BreakingChange> breakingChanges = breakChecker.check(oldApiSpec, newApiSpec);
        log.info("Check is finished");
        return breakingChanges;
    }
}
