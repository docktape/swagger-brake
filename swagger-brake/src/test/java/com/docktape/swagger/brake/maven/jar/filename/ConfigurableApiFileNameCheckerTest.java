package com.docktape.swagger.brake.maven.jar.filename;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ConfigurableApiFileNameCheckerTest {
    @Test
    void testConstructorShouldThrowExceptionIfNullParameterIsGiven() {
        // given
        // when
        assertThatThrownBy(() -> new ConfigurableApiFileNameChecker(null)).isExactlyInstanceOf(IllegalArgumentException.class);
        // then exception thrown
    }

    @Test
    void testConstructorShouldThrowExceptionIfEmptyStringParameterIsGiven() {
        // given
        // when
        assertThatThrownBy(() -> new ConfigurableApiFileNameChecker("  ")).isExactlyInstanceOf(IllegalArgumentException.class);
        // then exception thrown
    }

    @Test
    void testIsApiShouldReturnFalseWhenDoesntMatch() {
        // given
        ConfigurableApiFileNameChecker underTest = new ConfigurableApiFileNameChecker("openapi");
        // when
        boolean result = underTest.isApiFile("c:/api/swagger.yaml");
        // then
        assertThat(result).isFalse();
    }

    @Test
    void testIsApiShouldReturnTrueWhenYamlMatchesFullPath() {
        // given
        ConfigurableApiFileNameChecker underTest = new ConfigurableApiFileNameChecker("openapi");
        // when
        boolean result = underTest.isApiFile("c:/api/openapi.yaml");
        // then
        assertThat(result).isTrue();
    }

    @Test
    void testIsApiShouldReturnTrueWhenYamlMatches() {
        // given
        ConfigurableApiFileNameChecker underTest = new ConfigurableApiFileNameChecker("openapi");
        // when
        boolean result = underTest.isApiFile("openapi.yaml");
        // then
        assertThat(result).isTrue();
    }

    @Test
    void testIsApiShouldReturnTrueWhenYmlMatches() {
        // given
        ConfigurableApiFileNameChecker underTest = new ConfigurableApiFileNameChecker("openapi");
        // when
        boolean result = underTest.isApiFile("openapi.yml");
        // then
        assertThat(result).isTrue();
    }

    @Test
    void testIsApiShouldReturnTrueWhenJsonMatches() {
        // given
        ConfigurableApiFileNameChecker underTest = new ConfigurableApiFileNameChecker("openapi");
        // when
        boolean result = underTest.isApiFile("openapi.json");
        // then
        assertThat(result).isTrue();
    }

    @Test
    void testIsApiShouldReturnTrueWhenExtensionIsProvidedAndMatches() {
        // given
        ConfigurableApiFileNameChecker underTest = new ConfigurableApiFileNameChecker("openapi.txt");
        // when
        boolean result = underTest.isApiFile("openapi.txt");
        // then
        assertThat(result).isTrue();
    }
}