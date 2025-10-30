package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

public class ApiFilenameHandlerTest {
    private ApiFilenameHandler underTest = new ApiFilenameHandler();

    @Test
    public void testHandleWorks() {
        // given
        String propertyValue = "something";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getApiFilename).isEqualTo(propertyValue);
    }

    @Test
    public void testHandleDoesNotDoAnythingIfPropertyIsNull() {
        // given
        String propertyValue = null;
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getApiFilename).isNull();
    }

    @Test
    public void testHandleDoesNotDoAnythingIfPropertyIsEmpty() {
        // given
        String propertyValue = "";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getApiFilename).isNull();
    }

    @Test
    public void testHandleDoesNotDoAnythingIfPropertyIsBlank() {
        // given
        String propertyValue = "   ";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getApiFilename).isNull();
    }

    @Test
    public void testGetHandledCliOptionIsCorrect() {
        // given
        // when
        CliOption result = underTest.getHandledCliOption();
        // then
        assertThat(result).isEqualTo(CliOption.API_FILENAME);
    }
}