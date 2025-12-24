package com.docktape.swagger.brake.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PathNormalizerTest {

    @Test
    void testNormalizePathSlashesThrowsExceptionWhenNullGiven() {
        // given
        // when
        assertThatThrownBy(() -> PathNormalizer.normalizePathSlashes(null)).isExactlyInstanceOf(IllegalArgumentException.class);
        // then exception thrown
    }

    @Test
    void testNormalizePathSlashesThrowsExceptionWhenBlankGiven() {
        // given
        // when
        assertThatThrownBy(() -> PathNormalizer.normalizePathSlashes("   ")).isExactlyInstanceOf(IllegalArgumentException.class);
        // then exception thrown
    }

    @Test
    void testNormalizePathSlashesAddsNecessaryLeadingSlashesIfMissing() {
        // given
        // when
        String result = PathNormalizer.normalizePathSlashes("test/path");
        // then
        assertThat(result).isEqualTo("/test/path");
    }

    @Test
    void testNormalizePathSlashesRemovesUnnecessaryTrailingSlashesIfAny() {
        // given
        // when
        String result = PathNormalizer.normalizePathSlashes("/test/path/");
        // then
        assertThat(result).isEqualTo("/test/path");
    }

    @Test
    void testNormalizePathSlashesRemovesAndAddsSlashesWhereNeeded() {
        // given
        // when
        String result = PathNormalizer.normalizePathSlashes("test/path/");
        // then
        assertThat(result).isEqualTo("/test/path");
    }
}