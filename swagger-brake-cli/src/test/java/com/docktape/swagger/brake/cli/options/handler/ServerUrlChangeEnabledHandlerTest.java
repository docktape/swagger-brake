package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

class ServerUrlChangeEnabledHandlerTest {
    private final ServerUrlChangeEnabledHandler underTest = new ServerUrlChangeEnabledHandler();

    @Test
    void testHandleShouldLeaveNullWhenNullValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle(null, options);

        // then
        assertThat(options.getServerUrlChangeEnabled()).isNull();
    }

    @Test
    void testHandleShouldLeaveNullWhenEmptyValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("", options);

        // then
        assertThat(options.getServerUrlChangeEnabled()).isNull();
    }

    @Test
    void testHandleShouldSetTrueWhenTrueValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("true", options);

        // then
        assertThat(options.getServerUrlChangeEnabled()).isTrue();
    }

    @Test
    void testHandleShouldSetFalseWhenFalseValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("false", options);

        // then
        assertThat(options.getServerUrlChangeEnabled()).isFalse();
    }

    @Test
    void testHandleShouldLeaveNullWhenRandomValueGiven() {
        // given
        Options options = new Options();

        // when
        underTest.handle("random", options);

        // then
        assertThat(options.getServerUrlChangeEnabled()).isNull();
    }
}
