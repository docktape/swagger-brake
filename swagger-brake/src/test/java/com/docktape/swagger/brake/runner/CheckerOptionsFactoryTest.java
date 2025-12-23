package com.docktape.swagger.brake.runner;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.core.CheckerOptions;
import org.junit.jupiter.api.Test;

class CheckerOptionsFactoryTest {
    private CheckerOptionsFactory underTest = new CheckerOptionsFactory();

    @Test
    void testCreateShouldLeaveTrueValueForDeprecatedApiDeletionAllowedWhenNullOptionGiven() {
        // given
        Options options = new Options();
        options.setDeprecatedApiDeletionAllowed(null);

        // when
        CheckerOptions result = underTest.create(options);

        // then
        assertThat(result.isDeprecatedApiDeletionAllowed()).isTrue();
    }

    @Test
    void testCreateShouldLeaveTrueValueForDeprecatedApiDeletionAllowedWhenTrueOptionGiven() {
        // given
        Options options = new Options();
        options.setDeprecatedApiDeletionAllowed(true);

        // when
        CheckerOptions result = underTest.create(options);

        // then
        assertThat(result.isDeprecatedApiDeletionAllowed()).isTrue();
    }

    @Test
    void testCreateShouldLeaveFalseValueForDeprecatedApiDeletionAllowedWhenFalseOptionGiven() {
        // given
        Options options = new Options();
        options.setDeprecatedApiDeletionAllowed(false);

        // when
        CheckerOptions result = underTest.create(options);

        // then
        assertThat(result.isDeprecatedApiDeletionAllowed()).isFalse();
    }
}