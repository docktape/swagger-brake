package com.docktape.swagger.brake.cli.options.handler;

import static com.google.common.collect.ImmutableSet.of;
import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import org.junit.jupiter.api.Test;

public class OutputFormatsHandlerTest {
    private OutputFormatsHandler underTest = new OutputFormatsHandler();

    @Test
    public void testHandleShouldSetFormatToStandardOutWhenPropertyValueIsNull() {
        // given
        Options options = new Options();
        // when
        underTest.handle(null, options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.STDOUT));
    }

    @Test
    public void testHandleShouldSetFormatToStandardOutWhenPropertyValueIsEmpty() {
        // given
        Options options = new Options();
        // when
        underTest.handle("", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.STDOUT));
    }

    @Test
    public void testHandleShouldSetFormatToStandardOutWhenPropertyValueIsNotMappable() {
        // given
        Options options = new Options();
        // when
        underTest.handle("something", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.STDOUT));
    }

    @Test
    public void testHandleShouldSetFormatToStandardOutWhenPropertyValueIsStdOut() {
        // given
        Options options = new Options();
        // when
        underTest.handle("stdout", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.STDOUT));
    }

    @Test
    public void testHandleShouldSetFormatToJsonWhenPropertyValueIsJson() {
        // given
        Options options = new Options();
        // when
        underTest.handle("json", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.JSON));
    }

    @Test
    public void testHandleShouldSetFormatToJsonAndHtmlWhenValueIsJsonCommaHtml() {
        // given
        Options options = new Options();
        // when
        underTest.handle("json,html", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.HTML, OutputFormat.JSON));
    }


    @Test
    public void testHandleShouldSetFormatToJsonAndHtmlWhenValueIsJsonCommaHtmlWithSpaces() {
        // given
        Options options = new Options();
        // when
        underTest.handle("json,       html", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.HTML, OutputFormat.JSON));
    }
}