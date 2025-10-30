package com.docktape.swagger.brake.report;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;

import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReporterFactory {
    private final Collection<CheckableReporter> reporters;

    /**
     * Creates a {@link Reporter} instance based on the {@link Options} provided.
     * @param options the {@link Options}.
     * @return the {@link Reporter} instance.
     */
    public Reporter create(Options options) {
        Collection<Reporter> reporters = options.getOutputFormats().stream().map(this::findReporters).flatMap(Collection::stream).collect(toSet());
        if (reporters.isEmpty()) {
            throw new IllegalStateException("No suitable reporters could be loaded");
        }
        log.debug("The following reporters will be used {}", reporters.stream().map(Reporter::getClass).map(Class::getSimpleName).collect(toList()));
        return new CompositeReporter(reporters);
    }

    private Collection<Reporter> findReporters(OutputFormat outputFormat) {
        return reporters.stream().filter(r -> r.canReport(outputFormat)).collect(toList());
    }
}
