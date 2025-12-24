package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VerboseHandlerTest {
    private final VerboseHandler underTest = new VerboseHandler();

    @Test
    void testHandleWorks() {
        // given
        String propertyValue = "";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).isEqualTo(new Options());
        Logger logger = LoggerFactory.getLogger("com.docktape.swagger.brake");
        assertThat(logger.isDebugEnabled()).isTrue();
    }

    @Test
    void testHandleDoesNotDoAnythingIfPropertyIsNull() {
        // given
        String propertyValue = null;
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getOutputFilePath).isNull();
    }

    @Test
    void testGetHandledCliOptionIsCorrect() {
        // given
        // when
        CliOption result = underTest.getHandledCliOption();
        // then
        assertThat(result).isEqualTo(CliOption.VERBOSE);
    }
}