package com.docktape.swagger.brake.report;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.runner.OutputFormat;
import org.junit.jupiter.api.Test;

public class StdOutReporterTest {
    private StdOutReporter underTest = new StdOutReporter();

    @Test
    public void testCanReportShouldReturnTrueIfOutputFormatIsStdOut() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.STDOUT);
        // then
        assertThat(result).isTrue();
    }

    @Test
    public void testCanReportShouldReturnFalseIfOutputFormatIsHtml() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.HTML);
        // then
        assertThat(result).isTrue();
    }
}