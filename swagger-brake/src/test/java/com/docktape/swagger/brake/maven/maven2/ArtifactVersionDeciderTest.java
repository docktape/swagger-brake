package com.docktape.swagger.brake.maven.maven2;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArtifactVersionDeciderTest {
    @Test
    void testIsSnapshotReturnsTrueWhenVersionIsEndingWithSnapshot() {
        // given
        // when
        boolean result = ArtifactVersionDecider.isSnapshot("1.0.0-SNAPSHOT");
        // then
        assertTrue(result);
    }

    @Test
    void testIsSnapshotReturnsFalseWhenVersionIsNotEndingWithSnapshot() {
        // given
        // when
        boolean result = ArtifactVersionDecider.isSnapshot("1.0.0");
        // then
        assertFalse(result);
    }

    @Test
    void testIsSnapshotReturnsFalseWhenVersionIsCompletelyRandom() {
        // given
        // when
        boolean result = ArtifactVersionDecider.isSnapshot("something-else-that-is-not-a-version");
        // then
        assertFalse(result);
    }
}