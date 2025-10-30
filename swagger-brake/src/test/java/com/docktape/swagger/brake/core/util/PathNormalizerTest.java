package com.docktape.swagger.brake.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class PathNormalizerTest {

    @Test
    public void testNormalizePathSlashesThrowsExceptionWhenNullGiven() {
        // given
        // when
        assertThatThrownBy(() -> PathNormalizer.normalizePathSlashes(null)).isExactlyInstanceOf(IllegalArgumentException.class);
        // then exception thrown
    }

    @Test
    public void testNormalizePathSlashesThrowsExceptionWhenBlankGiven() {
        // given
        // when
        assertThatThrownBy(() -> PathNormalizer.normalizePathSlashes("   ")).isExactlyInstanceOf(IllegalArgumentException.class);
        // then exception thrown
    }

    @Test
    public void testNormalizePathSlashesAddsNecessaryLeadingSlashesIfMissing() {
        // given
        // when
        String result = PathNormalizer.normalizePathSlashes("test/path");
        // then
        assertThat(result).isEqualTo("/test/path");
    }

    @Test
    public void testNormalizePathSlashesRemovesUnnecessaryTrailingSlashesIfAny() {
        // given
        // when
        String result = PathNormalizer.normalizePathSlashes("/test/path/");
        // then
        assertThat(result).isEqualTo("/test/path");
    }

    @Test
    public void testNormalizePathSlashesRemovesAndAddsSlashesWhereNeeded() {
        // given
        // when
        String result = PathNormalizer.normalizePathSlashes("test/path/");
        // then
        assertThat(result).isEqualTo("/test/path");
    }
}