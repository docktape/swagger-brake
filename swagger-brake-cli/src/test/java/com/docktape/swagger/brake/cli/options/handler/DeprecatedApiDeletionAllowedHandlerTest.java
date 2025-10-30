package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

public class DeprecatedApiDeletionAllowedHandlerTest {
    private DeprecatedApiDeletionAllowedHandler underTest = new DeprecatedApiDeletionAllowedHandler();

    @Test
    public void testHandleShouldGiveTrueValueForTheOptionWhenNullValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle(null, options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isNull();
    }

    @Test
    public void testHandleShouldGiveTrueValueForTheOptionWhenEmptyValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("", options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isNull();
    }

    @Test
    public void testHandleShouldGiveTrueValueForTheOptionWhenTrueValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("true", options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isTrue();
    }

    @Test
    public void testHandleShouldGiveTrueValueForTheOptionWhenRandomValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("asd", options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isNull();
    }

    @Test
    public void testHandleShouldGiveFalseValueForTheOptionWhenFalseValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("false", options);
        // then
        assertThat(options.getDeprecatedApiDeletionAllowed()).isFalse();
    }
}