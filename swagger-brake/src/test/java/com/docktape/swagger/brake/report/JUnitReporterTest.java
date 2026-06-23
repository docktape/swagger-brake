package com.docktape.swagger.brake.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.docktape.swagger.brake.core.ApiInfo;
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
class JUnitReporterTest {

    @Mock
    private FileWriter fileWriter;

    @Mock
    private DirectoryCreator directoryCreator;

    @InjectMocks
    private JUnitReporter underTest;

    @Test
    void testReportShouldWriteXmlFileWithFailureForEachBreakingChange() throws IOException {
        // given
        BreakingChange bc = mock(BreakingChange.class);
        when(bc.getRuleCode()).thenReturn("R001");
        when(bc.getMessage()).thenReturn("Path deleted: /api/v1/users");

        Collection<BreakingChange> breakingChanges = Collections.singletonList(bc);

        String outputFilePath = "outputFilePath";
        Options options = new Options();
        options.setOutputFilePath(outputFilePath);

        // when
        underTest.report(breakingChanges, options);

        // then
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        then(directoryCreator).should().create(outputFilePath);
        then(fileWriter).should().write(
            org.mockito.ArgumentMatchers.eq(outputFilePath + File.separator + "swagger-brake-junit.xml"),
            contentCaptor.capture()
        );

        String xml = contentCaptor.getValue();
        assertThat(xml).contains("<testsuites name=\"swagger-brake\">");
        assertThat(xml).contains("<testsuite name=\"BreakingChanges\" tests=\"1\" failures=\"1\">");
        assertThat(xml).contains("<testcase name=\"R001: Path deleted: /api/v1/users\" classname=\"swagger-brake\">");
        assertThat(xml).contains("<failure message=\"Path deleted: /api/v1/users\" type=\"BreakingChange\"/>");
    }

    @Test
    void testReportShouldWriteXmlFileWithVersionedFilenameWhenApiInfoHasVersion() throws IOException {
        // given
        BreakingChange bc = mock(BreakingChange.class);
        when(bc.getRuleCode()).thenReturn("R002");
        when(bc.getMessage()).thenReturn("Some breaking change");

        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setVersion("1.2.3");

        Options options = new Options();
        options.setOutputFilePath("out");

        // when
        underTest.report(Collections.singletonList(bc), Collections.emptyList(), options, apiInfo);

        // then
        then(fileWriter).should().write(
            org.mockito.ArgumentMatchers.eq("out" + File.separator + "swagger-brake-1.2.3-junit.xml"),
            org.mockito.ArgumentMatchers.anyString()
        );
    }

    @Test
    void testReportShouldProduceEmptyTestsuiteWhenNoBreakingChanges() throws IOException {
        // given
        Options options = new Options();
        options.setOutputFilePath("out");

        // when
        underTest.report(Collections.emptyList(), options);

        // then
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        then(fileWriter).should().write(org.mockito.ArgumentMatchers.anyString(), contentCaptor.capture());

        String xml = contentCaptor.getValue();
        assertThat(xml).contains("<testsuite name=\"BreakingChanges\" tests=\"0\" failures=\"0\">");
        assertThat(xml).doesNotContain("<testcase");
    }

    @Test
    void testReportShouldEscapeXmlSpecialCharactersInMessages() throws IOException {
        // given
        BreakingChange bc = mock(BreakingChange.class);
        when(bc.getRuleCode()).thenReturn("R003");
        when(bc.getMessage()).thenReturn("Field <name> changed to \"value\" & more");

        Options options = new Options();
        options.setOutputFilePath("out");

        // when
        underTest.report(Collections.singletonList(bc), options);

        // then
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        then(fileWriter).should().write(org.mockito.ArgumentMatchers.anyString(), contentCaptor.capture());

        String xml = contentCaptor.getValue();
        assertThat(xml).contains("&lt;name&gt;");
        assertThat(xml).contains("&quot;value&quot;");
        assertThat(xml).contains("&amp; more");
    }

    @Test
    void testReportShouldWriteMultipleTestcasesForMultipleBreakingChanges() throws IOException {
        // given
        BreakingChange bc1 = mock(BreakingChange.class);
        when(bc1.getRuleCode()).thenReturn("R001");
        when(bc1.getMessage()).thenReturn("First breaking change");

        BreakingChange bc2 = mock(BreakingChange.class);
        when(bc2.getRuleCode()).thenReturn("R002");
        when(bc2.getMessage()).thenReturn("Second breaking change");

        Options options = new Options();
        options.setOutputFilePath("out");

        // when
        underTest.report(List.of(bc1, bc2), options);

        // then
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        then(fileWriter).should().write(org.mockito.ArgumentMatchers.anyString(), contentCaptor.capture());

        String xml = contentCaptor.getValue();
        assertThat(xml).contains("<testsuite name=\"BreakingChanges\" tests=\"2\" failures=\"2\">");
        assertThat(xml).contains("R001: First breaking change");
        assertThat(xml).contains("R002: Second breaking change");
    }

    @Test
    void testCanReportShouldReturnTrueIfOutputFormatIsJunit() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.JUNIT);
        // then
        assertThat(result).isTrue();
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
    void testCanReportShouldReturnFalseIfOutputFormatIsJson() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.JSON);
        // then
        assertThat(result).isFalse();
    }
}
