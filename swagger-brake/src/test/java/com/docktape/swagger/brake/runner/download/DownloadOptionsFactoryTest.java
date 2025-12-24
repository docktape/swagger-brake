package com.docktape.swagger.brake.runner.download;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

class DownloadOptionsFactoryTest {
    private DownloadOptionsFactory underTest = new DownloadOptionsFactory();

    @Test
    void testCreateShouldWorkProperly() {
        // given
        String groupId = "groupId";
        String artifactId = "artifactId";
        String mavenRepoUrl = "mavenRepoUrl";
        String mavenSnapshotRepoUrl = "mavenSnapshotRepoUrl";
        String mavenRepoUsername = "username";
        String mavenRepoPassword = "password";
        String currentArtifactVersion = "1.2.0";

        Options options = new Options();
        options.setNewApiPath("newApi");
        options.setGroupId(groupId);
        options.setArtifactId(artifactId);
        options.setMavenRepoUrl(mavenRepoUrl);
        options.setMavenSnapshotRepoUrl(mavenSnapshotRepoUrl);
        options.setMavenRepoUsername(mavenRepoUsername);
        options.setMavenRepoPassword(mavenRepoPassword);
        options.setCurrentArtifactVersion(currentArtifactVersion);
        // when
        DownloadOptions result = underTest.create(options);
        // then
        assertThat(result).extracting(DownloadOptions::getRepoUrl).isEqualTo(mavenRepoUrl);
        assertThat(result).extracting(DownloadOptions::getSnapshotRepoUrl).isEqualTo(mavenSnapshotRepoUrl);
        assertThat(result).extracting(DownloadOptions::getGroupId).isEqualTo(groupId);
        assertThat(result).extracting(DownloadOptions::getArtifactId).isEqualTo(artifactId);
        assertThat(result).extracting(DownloadOptions::getUsername).isEqualTo(mavenRepoUsername);
        assertThat(result).extracting(DownloadOptions::getPassword).isEqualTo(mavenRepoPassword);
        assertThat(result).extracting(DownloadOptions::getCurrentArtifactVersion).isEqualTo(currentArtifactVersion);
    }
}