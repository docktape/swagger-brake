package com.docktape.swagger.brake.cli.options.handler;

import static com.google.common.collect.ImmutableSet.of;
import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.OutputFormat;
import org.junit.jupiter.api.Test;

class OutputFormatsHandlerTest {
    private final OutputFormatsHandler underTest = new OutputFormatsHandler();

    @Test
    void testHandleShouldSetFormatToStandardOutWhenPropertyValueIsNull() {
        // given
        Options options = new Options();
        // when
        underTest.handle(null, options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.STDOUT));
    }

    @Test
    void testHandleShouldSetFormatToStandardOutWhenPropertyValueIsEmpty() {
        // given
        Options options = new Options();
        // when
        underTest.handle("", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.STDOUT));
    }

    @Test
    void testHandleShouldSetFormatToStandardOutWhenPropertyValueIsNotMappable() {
        // given
        Options options = new Options();
        // when
        underTest.handle("something", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.STDOUT));
    }

    @Test
    void testHandleShouldSetFormatToStandardOutWhenPropertyValueIsStdOut() {
        // given
        Options options = new Options();
        // when
        underTest.handle("stdout", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.STDOUT));
    }

    @Test
    void testHandleShouldSetFormatToJsonWhenPropertyValueIsJson() {
        // given
        Options options = new Options();
        // when
        underTest.handle("json", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.JSON));
    }

    @Test
    void testHandleShouldSetFormatToJsonAndHtmlWhenValueIsJsonCommaHtml() {
        // given
        Options options = new Options();
        // when
        underTest.handle("json,html", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.HTML, OutputFormat.JSON));
    }

    @Test
    void testHandleShouldSetFormatToJsonAndHtmlWhenValueIsJsonCommaHtmlWithSpaces() {
        // given
        Options options = new Options();
        // when
        underTest.handle("json,       html", options);
        // then
        assertThat(options.getOutputFormats()).isEqualTo(of(OutputFormat.HTML, OutputFormat.JSON));
    }
}