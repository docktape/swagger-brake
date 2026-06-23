package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.core.Severity;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

class FailOnSeverityHandlerTest {
    private final FailOnSeverityHandler underTest = new FailOnSeverityHandler();

    @Test
    void testHandleShouldSetErrorSeverityWhenValueIsError() {
        // given
        Options options = new Options();
        // when
        underTest.handle("error", options);
        // then
        assertThat(options.getFailOnSeverity()).isEqualTo(Severity.ERROR);
    }

    @Test
    void testHandleShouldSetWarningSeverityWhenValueIsWarning() {
        // given
        Options options = new Options();
        // when
        underTest.handle("warning", options);
        // then
        assertThat(options.getFailOnSeverity()).isEqualTo(Severity.WARNING);
    }

    @Test
    void testHandleShouldSetInfoSeverityWhenValueIsInfo() {
        // given
        Options options = new Options();
        // when
        underTest.handle("info", options);
        // then
        assertThat(options.getFailOnSeverity()).isEqualTo(Severity.INFO);
    }

    @Test
    void testHandleShouldBeCaseInsensitive() {
        // given
        Options options = new Options();
        // when
        underTest.handle("WARNING", options);
        // then
        assertThat(options.getFailOnSeverity()).isEqualTo(Severity.WARNING);
    }

    @Test
    void testHandleShouldKeepDefaultWhenValueIsNull() {
        // given
        Options options = new Options();
        // when
        underTest.handle(null, options);
        // then
        assertThat(options.getFailOnSeverity()).isEqualTo(Severity.ERROR);
    }

    @Test
    void testHandleShouldKeepDefaultWhenValueIsBlank() {
        // given
        Options options = new Options();
        // when
        underTest.handle("  ", options);
        // then
        assertThat(options.getFailOnSeverity()).isEqualTo(Severity.ERROR);
    }

    @Test
    void testHandleShouldKeepDefaultWhenValueIsInvalid() {
        // given
        Options options = new Options();
        // when
        underTest.handle("invalid", options);
        // then
        assertThat(options.getFailOnSeverity()).isEqualTo(Severity.ERROR);
    }

    @Test
    void testGetHandledCliOptionShouldReturnFailOnSeverity() {
        assertThat(underTest.getHandledCliOption()).isEqualTo(CliOption.FAIL_ON_SEVERITY);
    }
}
