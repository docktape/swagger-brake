package com.docktape.swagger.brake.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.report.file.DirectoryCreator;
import com.docktape.swagger.brake.report.file.FileWriter;
import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarkdownReporterTest {

    @Mock
    private FileWriter fileWriter;

    @Mock
    private DirectoryCreator directoryCreator;

    @InjectMocks
    private MarkdownReporter underTest;

    @Test
    void testCanReportShouldReturnTrueIfOutputFormatIsMarkdown() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.MARKDOWN);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void testCanReportShouldReturnFalseIfOutputFormatIsJson() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.JSON);
        // then
        assertThat(result).isFalse();
    }

    @Test
    void testCanReportShouldReturnFalseIfOutputFormatIsHtml() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.HTML);
        // then
        assertThat(result).isFalse();
    }

    @Test
    void testReportShouldWriteMarkdownFileWithBreakingChanges() throws IOException {
        // given
        BreakingChange bc = mock(BreakingChange.class);
        given(bc.getRuleCode()).willReturn("R004");
        given(bc.getMessage()).willReturn("parameter X deleted at GET /foo");

        Collection<BreakingChange> breakingChanges = Collections.singletonList(bc);

        String outputFilePath = "outputFilePath";
        Options options = new Options();
        options.setOutputFilePath(outputFilePath);

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        // when
        underTest.report(breakingChanges, options);
        // then
        then(directoryCreator).should().create(outputFilePath);
        then(fileWriter).should().write(eq(outputFilePath + File.separator + "swagger-brake.md"), contentCaptor.capture());
        String content = contentCaptor.getValue();
        assertThat(content).contains("# swagger-brake Report");
        assertThat(content).contains("## Breaking Changes (1)");
        assertThat(content).contains("| R004 | parameter X deleted at GET /foo |");
        assertThat(content).contains("## Ignored Breaking Changes (0)");
    }

    @Test
    void testReportShouldWriteMarkdownFileWithNoBreakingChanges() throws IOException {
        // given
        Collection<BreakingChange> breakingChanges = Collections.emptyList();

        String outputFilePath = "outputFilePath";
        Options options = new Options();
        options.setOutputFilePath(outputFilePath);

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        // when
        underTest.report(breakingChanges, options);
        // then
        then(fileWriter).should().write(anyString(), contentCaptor.capture());
        String content = contentCaptor.getValue();
        assertThat(content).contains("## Breaking Changes (0)");
        assertThat(content).doesNotContain("R0");
    }

    @Test
    void testReportShouldEscapePipeCharactersInMessages() throws IOException {
        // given
        BreakingChange bc = mock(BreakingChange.class);
        given(bc.getRuleCode()).willReturn("R010");
        given(bc.getMessage()).willReturn("value changed from a|b to c|d");

        Collection<BreakingChange> breakingChanges = Collections.singletonList(bc);

        String outputFilePath = "outputFilePath";
        Options options = new Options();
        options.setOutputFilePath(outputFilePath);

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        // when
        underTest.report(breakingChanges, options);
        // then
        then(fileWriter).should().write(anyString(), contentCaptor.capture());
        String content = contentCaptor.getValue();
        assertThat(content).contains("value changed from a\\|b to c\\|d");
    }

    @Test
    void testReportShouldWriteIgnoredBreakingChangesSection() throws IOException {
        // given
        BreakingChange bc = mock(BreakingChange.class);
        given(bc.getRuleCode()).willReturn("R001");
        given(bc.getMessage()).willReturn("some ignored change");

        Collection<BreakingChange> breakingChanges = Collections.emptyList();
        List<BreakingChange> ignoredBreakingChanges = Collections.singletonList(bc);

        String outputFilePath = "outputFilePath";
        Options options = new Options();
        options.setOutputFilePath(outputFilePath);

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        // when
        underTest.report(breakingChanges, ignoredBreakingChanges, options, null);
        // then
        then(fileWriter).should().write(anyString(), contentCaptor.capture());
        String content = contentCaptor.getValue();
        assertThat(content).contains("## Breaking Changes (0)");
        assertThat(content).contains("## Ignored Breaking Changes (1)");
        assertThat(content).contains("| R001 | some ignored change |");
    }
}
