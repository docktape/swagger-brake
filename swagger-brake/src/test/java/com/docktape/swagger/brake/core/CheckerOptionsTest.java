package com.docktape.swagger.brake.core;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class CheckerOptionsTest {

    @Test
    void testDefaultMaxLogSerializationDepth() {
        CheckerOptions options = new CheckerOptions();
        assertEquals(3, options.getMaxLogSerializationDepth());
    }

    @Test
    void testSetMaxLogSerializationDepthWithValidValue() {
        CheckerOptions options = new CheckerOptions();
        options.setMaxLogSerializationDepth(5);
        assertEquals(5, options.getMaxLogSerializationDepth());
    }

    @Test
    void testSetMaxLogSerializationDepthWithMinValidValue() {
        CheckerOptions options = new CheckerOptions();
        options.setMaxLogSerializationDepth(1);
        assertEquals(1, options.getMaxLogSerializationDepth());
    }

    @Test
    void testSetMaxLogSerializationDepthWithMaxValidValue() {
        CheckerOptions options = new CheckerOptions();
        options.setMaxLogSerializationDepth(20);
        assertEquals(20, options.getMaxLogSerializationDepth());
    }

    @Test
    void testSetMaxLogSerializationDepthWithZeroThrowsException() {
        CheckerOptions options = new CheckerOptions();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            options.setMaxLogSerializationDepth(0);
        });
        assertAll(
                () -> assertTrue(exception.getMessage().contains("must be between 1 and 20")),
                () -> assertTrue(exception.getMessage().contains("0"))
        );
    }

    @Test
    void testSetMaxLogSerializationDepthWithNegativeValueThrowsException() {
        CheckerOptions options = new CheckerOptions();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            options.setMaxLogSerializationDepth(-1);
        });
        assertAll(
                () -> assertTrue(exception.getMessage().contains("must be between 1 and 20")),
                () -> assertTrue(exception.getMessage().contains("-1"))
        );
    }

    @Test
    void testSetMaxLogSerializationDepthWithTooLargeValueThrowsException() {
        CheckerOptions options = new CheckerOptions();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            options.setMaxLogSerializationDepth(21);
        });
        assertAll(
                () -> assertTrue(exception.getMessage().contains("must be between 1 and 20")),
                () -> assertTrue(exception.getMessage().contains("21"))
        );
    }
}
