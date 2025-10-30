package com.docktape.swagger.brake.maven.maven2;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ArtifactVersionDeciderTest {
    @Test
    public void testIsSnapshotReturnsTrueWhenVersionIsEndingWithSnapshot() {
        // given
        // when
        boolean result = ArtifactVersionDecider.isSnapshot("1.0.0-SNAPSHOT");
        // then
        assertTrue(result);
    }

    @Test
    public void testIsSnapshotReturnsFalseWhenVersionIsNotEndingWithSnapshot() {
        // given
        // when
        boolean result = ArtifactVersionDecider.isSnapshot("1.0.0");
        // then
        assertFalse(result);
    }

    @Test
    public void testIsSnapshotReturnsFalseWhenVersionIsCompletelyRandom() {
        // given
        // when
        boolean result = ArtifactVersionDecider.isSnapshot("something-else-that-is-not-a-version");
        // then
        assertFalse(result);
    }
}