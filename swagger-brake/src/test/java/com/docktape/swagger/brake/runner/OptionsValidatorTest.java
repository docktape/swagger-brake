package com.docktape.swagger.brake.runner;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionsValidatorTest {
    private OptionsValidator underTest = new OptionsValidator();

    @Test
    public void testValidateThrowsExceptionWhenNewApiPathIsNotSet() {
        // given
        Options options = new Options();
        options.setOldApiPath("something");
        // when
        Throwable result = Assertions.catchThrowable(() -> underTest.validate(options));
        // then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
        assertThat(result.getMessage()).contains("newApiPath");
    }

    @Test
    public void testValidateThrowsExceptionWhenNeitherOldApiPathNorMavenIsSet() {
        // given
        Options options = new Options();
        options.setNewApiPath("something");
        // when
        Throwable result = Assertions.catchThrowable(() -> underTest.validate(options));
        // then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
        assertThat(result.getMessage()).contains("oldApiPath");
        assertThat(result.getMessage()).containsIgnoringCase("maven");
    }

    @Test
    public void testValidateWorksWhenOldAndNewApiIsSet() {
        // given
        Options options = new Options();
        options.setNewApiPath("something");
        options.setOldApiPath("something");
        // when
        underTest.validate(options);
        // then no exception
    }

    @Test
    public void testValidateShouldNotThrowExceptionWhenMavenRepoUrlIsSetButSnapshotRepoIsNot() {
        // given
        Options options = new Options();
        options.setNewApiPath("something");
        options.setMavenRepoUrl("localhost:8080/repo");
        options.setCurrentArtifactVersion("1.0.0-SNAPSHOT");
        options.setGroupId("com.docktape");
        options.setArtifactId("swagger-brake");
        // when
        Throwable result = Assertions.catchThrowable(() -> underTest.validate(options));
        // then
        assertThat(result).isNull();
    }

    @Test
    public void testValidateShouldNotThrowExceptionWhenMavenSnapshotRepoUrlIsSetButReleaseRepoIsNot() {
        // given
        Options options = new Options();
        options.setNewApiPath("something");
        options.setMavenSnapshotRepoUrl("localhost:8080/repo");
        options.setCurrentArtifactVersion("1.0.0-SNAPSHOT");
        options.setGroupId("com.docktape");
        options.setArtifactId("swagger-brake");
        // when
        Throwable result = Assertions.catchThrowable(() -> underTest.validate(options));
        // then
        assertThat(result).isNull();
    }

    @Test
    public void testValidateThrowsExceptionWhenMavenConfigIsSetExceptCurrentArtifactVersion() {
        // given
        Options options = new Options();
        options.setNewApiPath("something");
        options.setMavenRepoUrl("localhost:8080/repo");
        options.setMavenSnapshotRepoUrl("localhost:8080/snapshot-repo");
        options.setGroupId("com.docktape");
        options.setArtifactId("swagger-brake");
        // when
        Throwable result = Assertions.catchThrowable(() -> underTest.validate(options));
        // then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
        assertThat(result.getMessage()).contains("currentArtifactVersion");
    }

    @Test
    public void testValidateThrowsExceptionWhenMavenConfigIsSetExceptGroupId() {
        // given
        Options options = new Options();
        options.setNewApiPath("something");
        options.setMavenRepoUrl("localhost:8080/repo");
        options.setMavenSnapshotRepoUrl("localhost:8080/snapshot-repo");
        options.setCurrentArtifactVersion("1.0.0-SNAPSHOT");
        options.setArtifactId("swagger-brake");
        // when
        Throwable result = Assertions.catchThrowable(() -> underTest.validate(options));
        // then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
        assertThat(result.getMessage()).contains("groupId");
    }

    @Test
    public void testValidateThrowsExceptionWhenMavenConfigIsSetExceptArtifactId() {
        // given
        Options options = new Options();
        options.setNewApiPath("something");
        options.setMavenRepoUrl("localhost:8080/repo");
        options.setMavenSnapshotRepoUrl("localhost:8080/snapshot-repo");
        options.setCurrentArtifactVersion("1.0.0-SNAPSHOT");
        options.setGroupId("com.docktape");
        // when
        Throwable result = Assertions.catchThrowable(() -> underTest.validate(options));
        // then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
        assertThat(result.getMessage()).contains("artifactId");
    }

    @Test
    public void testValidateShouldNotThrowExceptionWhenMavenIsConfigured() {
        // given
        Options options = new Options();
        options.setNewApiPath("something");
        options.setMavenRepoUrl("localhost:8080/repo");
        options.setMavenSnapshotRepoUrl("localhost:8080/snapshot-repo");
        options.setCurrentArtifactVersion("1.0.0-SNAPSHOT");
        options.setGroupId("com.docktape");
        options.setArtifactId("swagger-brake");
        // when
        underTest.validate(options);
        // then no exception
    }

    @Test
    public void testValidateDoesNotThrowsExceptionWhenOldApiPathAndMavenIsConfigured() {
        // given
        Options options = new Options();
        options.setOldApiPath("somethingelse");
        options.setNewApiPath("something");
        options.setCurrentArtifactVersion("1.0.0-SNAPSHOT");
        options.setGroupId("com.docktape");
        options.setArtifactId("swagger-brake");
        // when
        underTest.validate(options);
        // then no exception
    }
}
