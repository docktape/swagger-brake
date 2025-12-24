package com.docktape.swagger.brake.report;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.runner.OutputFormat;
import org.junit.jupiter.api.Test;

class StdOutReporterTest {
    private StdOutReporter underTest = new StdOutReporter();

    @Test
    void testCanReportShouldReturnTrueIfOutputFormatIsStdOut() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.STDOUT);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void testCanReportShouldReturnFalseIfOutputFormatIsHtml() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.HTML);
        // then
        assertThat(result).isTrue();
    }
}