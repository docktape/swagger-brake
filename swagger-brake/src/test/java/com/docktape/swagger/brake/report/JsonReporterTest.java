package com.docktape.swagger.brake.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.report.file.DirectoryCreator;
import com.docktape.swagger.brake.report.file.FileWriter;
import com.docktape.swagger.brake.report.json.JsonConverter;
import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JsonReporterTest {

    @Mock
    private FileWriter fileWriter;

    @Mock
    private JsonConverter jsonConverter;

    @Mock
    private DirectoryCreator directoryCreator;

    @InjectMocks
    private JsonReporter underTest;

    @Test
    public void testReportShouldWorkCorrectly() throws IOException {
        // given
        BreakingChange bc = mock(BreakingChange.class);

        Collection<BreakingChange> breakingChanges = Collections.singletonList(bc);

        String outputFilePath = "outputFilePath";
        Options options = new Options();
        options.setOutputFilePath(outputFilePath);

        String content = "content";
        given(jsonConverter.convert(any())).willReturn(content);
        given(bc.getRuleCode()).willReturn("RXY");
        // when
        underTest.report(breakingChanges, options);
        // then
        then(directoryCreator).should().create(outputFilePath);
        then(fileWriter).should().write(outputFilePath + File.separator + "swagger-brake.json", content);
    }

    @Test
    public void testCanReportShouldReturnTrueIfOutputFormatIsJson() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.JSON);
        // then
        assertThat(result).isTrue();
    }

    @Test
    public void testCanReportShouldReturnFalseIfOutputFormatIsHtml() {
        // given
        // when
        boolean result = underTest.canReport(OutputFormat.HTML);
        // then
        assertThat(result).isFalse();
    }
}