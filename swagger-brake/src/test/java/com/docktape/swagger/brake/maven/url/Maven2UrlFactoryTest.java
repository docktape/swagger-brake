package com.docktape.swagger.brake.maven.url;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.maven2.Maven2UrlFactory;
import com.docktape.swagger.brake.runner.ArtifactPackaging;
import org.junit.jupiter.api.Test;

class Maven2UrlFactoryTest {
    private Maven2UrlFactory underTest = new Maven2UrlFactory();

    @Test
    void testCreateLatestArtifactSnapshotMetadataUrlShouldWorkProperlyForSnapshot() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setGroupId("io.swagger.brake");
        options.setArtifactId("swagger-brake-example");
        options.setSnapshotRepoUrl("http://localhost:8081/artifactory/libs-snapshot-local");
        options.setCurrentArtifactVersion("1.2.0-SNAPSHOT");
        options.setArtifactPackaging(ArtifactPackaging.JAR);
        String latestVersion = "1.2.0-SNAPSHOT";

        String expected = "http://localhost:8081/artifactory/libs-snapshot-local/io/swagger/brake/swagger-brake-example/1.2.0-SNAPSHOT/maven-metadata.xml";
        // when
        String result = underTest.createLatestArtifactSnapshotMetadataUrl(options, latestVersion);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreateLatestArtifactVersionMetadataUrlShouldWorkProperlyForSnapshot() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setGroupId("io.swagger.brake");
        options.setArtifactId("swagger-brake-example");
        options.setSnapshotRepoUrl("http://localhost:8081/artifactory/libs-snapshot-local");
        options.setCurrentArtifactVersion("1.2.0-SNAPSHOT");
        options.setArtifactPackaging(ArtifactPackaging.JAR);

        String expected = "http://localhost:8081/artifactory/libs-snapshot-local/io/swagger/brake/swagger-brake-example/maven-metadata.xml";
        // when
        String result = underTest.createLatestArtifactVersionMetadataUrl(options);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreateLatestArtifactUrlShouldWorkProperlyForSnapshot() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setGroupId("io.swagger.brake");
        options.setArtifactId("swagger-brake-example");
        options.setSnapshotRepoUrl("http://localhost:8081/artifactory/libs-snapshot-local");
        options.setCurrentArtifactVersion("1.2.0-SNAPSHOT");
        options.setArtifactPackaging(ArtifactPackaging.JAR);
        String latestVersion = "1.2.0-SNAPSHOT";
        String latestSnapshotName = "swagger-brake-example-1.2.0-20181118.221307-1";

        String expected =
            "http://localhost:8081/artifactory/libs-snapshot-local/io/swagger/brake/swagger-brake-example/1.2.0-SNAPSHOT/swagger-brake-example-1.2.0-20181118.221307-1.jar";
        // when
        String result = underTest.createLatestArtifactUrl(options, latestSnapshotName, latestVersion);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreateLatestArtifactUrlShouldWorkProperlyForSnapshotWithWarPackaging() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setGroupId("io.swagger.brake");
        options.setArtifactId("swagger-brake-example");
        options.setSnapshotRepoUrl("http://localhost:8081/artifactory/libs-snapshot-local");
        options.setCurrentArtifactVersion("1.2.0-SNAPSHOT");
        options.setArtifactPackaging(ArtifactPackaging.WAR);
        String latestVersion = "1.2.0-SNAPSHOT";
        String latestSnapshotName = "swagger-brake-example-1.2.0-20181118.221307-1";

        String expected =
            "http://localhost:8081/artifactory/libs-snapshot-local/io/swagger/brake/swagger-brake-example/1.2.0-SNAPSHOT/swagger-brake-example-1.2.0-20181118.221307-1.war";
        // when
        String result = underTest.createLatestArtifactUrl(options, latestSnapshotName, latestVersion);
        // then
        assertThat(result).isEqualTo(expected);
    }


    @Test
    void testCreateLatestArtifactVersionMetadataUrlShouldWorkProperlyForRelease() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setGroupId("io.swagger.brake");
        options.setArtifactId("swagger-brake-example");
        options.setRepoUrl("http://localhost:8081/artifactory/libs-release-local");
        options.setCurrentArtifactVersion("1.3.0");
        options.setArtifactPackaging(ArtifactPackaging.JAR);

        String expected = "http://localhost:8081/artifactory/libs-release-local/io/swagger/brake/swagger-brake-example/maven-metadata.xml";
        // when
        String result = underTest.createLatestArtifactVersionMetadataUrl(options);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreateLatestArtifactUrlShouldWorkProperlyForRelease() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setGroupId("io.swagger.brake");
        options.setArtifactId("swagger-brake-example");
        options.setRepoUrl("http://localhost:8081/artifactory/libs-release-local");
        options.setCurrentArtifactVersion("1.3.0");
        options.setArtifactPackaging(ArtifactPackaging.JAR);
        String latestVersion = "1.2.0";
        String latestArtifactName = "swagger-brake-example-1.2.0";

        String expected = "http://localhost:8081/artifactory/libs-release-local/io/swagger/brake/swagger-brake-example/1.2.0/swagger-brake-example-1.2.0.jar";
        // when
        String result = underTest.createLatestArtifactUrl(options, latestArtifactName, latestVersion);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreateLatestArtifactUrlShouldWorkProperlyForReleaseWithWarPackaging() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setGroupId("io.swagger.brake");
        options.setArtifactId("swagger-brake-example");
        options.setRepoUrl("http://localhost:8081/artifactory/libs-release-local");
        options.setCurrentArtifactVersion("1.3.0");
        options.setArtifactPackaging(ArtifactPackaging.WAR);
        String latestVersion = "1.2.0";
        String latestArtifactName = "swagger-brake-example-1.2.0";

        String expected = "http://localhost:8081/artifactory/libs-release-local/io/swagger/brake/swagger-brake-example/1.2.0/swagger-brake-example-1.2.0.war";
        // when
        String result = underTest.createLatestArtifactUrl(options, latestArtifactName, latestVersion);
        // then
        assertThat(result).isEqualTo(expected);
    }
}