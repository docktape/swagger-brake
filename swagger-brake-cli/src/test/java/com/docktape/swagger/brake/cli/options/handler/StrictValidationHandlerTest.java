package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrictValidationHandlerTest {
    private final StrictValidationHandler underTest = new StrictValidationHandler();

    @Test
    void testGetHandledCliOptionIsCorrect() {
        // given
        // when
        var result = underTest.getHandledCliOption();
        // then
        assertEquals(CliOption.STRICT_VALIDATION, result);
    }

    @Test
    void testHandleWorks() {
        // given
        String propertyValue = null;
        var options = new com.docktape.swagger.brake.runner.Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertTrue(options.getStrictValidation());
    }

    @ParameterizedTest(name = "#{index} - args={0}")
    @ValueSource(strings = {"TRUE", "True", "true"})
    void testHandleWorks(String propertyValue) {
        // given propertyValue
        var options = new com.docktape.swagger.brake.runner.Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertTrue(options.getStrictValidation());
    }

    @ParameterizedTest(name = "#{index} - args={0}")
    @ValueSource(strings = {"FALSE", "False", "false", "unknown", "123", ""})
    void testHandleWorksForFalse(String propertyValue) {
        // given propertyValue
        var options = new com.docktape.swagger.brake.runner.Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertFalse(options.getStrictValidation());
    }
}
