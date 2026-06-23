package com.docktape.swagger.brake.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.Severity;
import com.docktape.swagger.brake.runner.Options;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StdOutReporterSeverityTest {
    private final StdOutReporter underTest = new StdOutReporter();

    private PrintStream originalErr;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        originalErr = System.err;
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent, true, StandardCharsets.UTF_8.name()));
    }

    @AfterEach
    void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    void testReportShouldIncludeSeverityInOutput() {
        // given
        BreakingChange bc = mock(BreakingChange.class);
        given(bc.getRuleCode()).willReturn("R002");
        given(bc.getMessage()).willReturn("Path /test GET has been deleted");
        given(bc.getSeverity()).willReturn(Severity.ERROR);

        Options options = new Options();
        // when
        underTest.report(List.of(bc), Collections.emptyList(), options, null);
        // then
        String output = errContent.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("[ERROR]");
        assertThat(output).contains("R002");
        assertThat(output).contains("Path /test GET has been deleted");
    }

    @Test
    void testReportShouldIncludeWarningSeverityInOutput() {
        // given
        BreakingChange bc = mock(BreakingChange.class);
        given(bc.getRuleCode()).willReturn("R002");
        given(bc.getMessage()).willReturn("some warning change");
        given(bc.getSeverity()).willReturn(Severity.WARNING);

        Options options = new Options();
        // when
        underTest.report(List.of(bc), Collections.emptyList(), options, null);
        // then
        String output = errContent.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("[WARNING]");
    }
}
