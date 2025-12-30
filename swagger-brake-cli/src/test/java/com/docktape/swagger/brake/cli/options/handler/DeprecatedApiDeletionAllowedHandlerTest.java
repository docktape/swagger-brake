package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

class DeprecatedApiDeletionAllowedHandlerTest {
    private final DeprecatedApiDeletionAllowedHandler underTest = new DeprecatedApiDeletionAllowedHandler();

    @Test
    void testHandleShouldGiveTrueValueForTheOptionWhenNullValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle(null, options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isNull();
    }

    @Test
    void testHandleShouldGiveTrueValueForTheOptionWhenEmptyValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("", options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isNull();
    }

    @Test
    void testHandleShouldGiveTrueValueForTheOptionWhenTrueValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("true", options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isTrue();
    }

    @Test
    void testHandleShouldGiveTrueValueForTheOptionWhenRandomValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("asd", options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isNull();
    }

    @Test
    void testHandleShouldGiveFalseValueForTheOptionWhenFalseValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("false", options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isFalse();
    }
}