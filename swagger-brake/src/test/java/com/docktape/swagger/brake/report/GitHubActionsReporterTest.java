package com.docktape.swagger.brake.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GitHubActionsReporterTest {
    private final GitHubActionsReporter underTest = new GitHubActionsReporter();
    private final ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(capturedOut, false, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testReportShouldPrintAnnotationForEachBreakingChange() {
        // given
        BreakingChange bc1 = mock(BreakingChange.class);
        given(bc1.getRuleCode()).willReturn("R004");
        given(bc1.getMessage()).willReturn("parameter X deleted at GET /foo");

        BreakingChange bc2 = mock(BreakingChange.class);
        given(bc2.getRuleCode()).willReturn("R007");
        given(bc2.getMessage()).willReturn("response field removed at POST /bar");

        // when
        underTest.report(Arrays.asList(bc1, bc2), Collections.emptyList(), new Options(), null);

        // then
        String output = capturedOut.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("::error title=Breaking Change (R004)::parameter X deleted at GET /foo");
        assertThat(output).contains("::error title=Breaking Change (R007)::response field removed at POST /bar");
    }

    @Test
    void testReportShouldPrintNothingWhenNoBreakingChanges() {
        // given
        // when
        underTest.report(Collections.emptyList(), Collections.emptyList(), new Options(), null);

        // then
        assertThat(capturedOut.toString(StandardCharsets.UTF_8)).isEmpty();
    }

    @Test
    void testCanReportShouldReturnTrueForGitHubActionsFormat() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.GITHUB_ACTIONS);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void testCanReportShouldReturnFalseForStdOutFormat() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.STDOUT);
        // then
        assertThat(result).isFalse();
    }

    @Test
    void testCanReportShouldReturnFalseForJsonFormat() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.JSON);
        // then
        assertThat(result).isFalse();
    }

    @Test
    void testCanReportShouldReturnFalseForHtmlFormat() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.HTML);
        // then
        assertThat(result).isFalse();
    }
}
