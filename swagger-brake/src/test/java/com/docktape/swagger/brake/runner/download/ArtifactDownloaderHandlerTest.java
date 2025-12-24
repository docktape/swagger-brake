package com.docktape.swagger.brake.runner.download;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.File;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.LatestArtifactDownloader;
import com.docktape.swagger.brake.maven.LatestArtifactDownloaderFactory;
import com.docktape.swagger.brake.maven.jar.ApiFileJarResolver;
import com.docktape.swagger.brake.maven.jar.ApiFileResolverParameter;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArtifactDownloaderHandlerTest {
    @Mock
    private LatestArtifactDownloaderFactory downloaderFactory;

    @Mock
    private ApiFileJarResolver swaggerFileResolver;

    @Mock
    private DownloadOptionsFactory downloadOptionsFactory;

    @InjectMocks
    private ArtifactDownloaderHandler underTest;

    @Test
    void testHandleShouldNotDoAnythingWhenLatestArtifactDownloadIsDisabled() {
        // given
        Options options = new Options();
        options.setOldApiPath("oldApi");
        options.setNewApiPath("newApi");
        // when
        underTest.handle(options);
        // then
        verifyNoMoreInteractions(downloaderFactory, swaggerFileResolver);
    }

    @Test
    void testHandleShouldWorkCorrectlyWhenLatestArtifactDownloadIsEnabled() {
        // given
        String groupId = "groupId";
        String artifactId = "artifactId";
        String mavenRepoUrl = "mavenRepoUrl";
        String mavenSnapshotRepoUrl = "mavenSnapshotRepoUrl";
        String mavenRepoUsername = "username";
        String mavenRepoPassword = "password";
        String apiFilename = "apiFilename";
        String currentArtifactVersion = "1.0.0-SNAPSHOT";

        DownloadOptions downloadOptions = new DownloadOptions();
        downloadOptions.setRepoUrl(mavenRepoUrl);
        downloadOptions.setSnapshotRepoUrl(mavenSnapshotRepoUrl);
        downloadOptions.setGroupId(groupId);
        downloadOptions.setArtifactId(artifactId);
        downloadOptions.setUsername(mavenRepoUsername);
        downloadOptions.setPassword(mavenRepoPassword);
        downloadOptions.setCurrentArtifactVersion(currentArtifactVersion);

        Options options = new Options();
        options.setNewApiPath("newApi");
        options.setGroupId(groupId);
        options.setArtifactId(artifactId);
        options.setMavenRepoUrl(mavenRepoUrl);
        options.setMavenSnapshotRepoUrl(mavenSnapshotRepoUrl);
        options.setMavenRepoUsername(mavenRepoUsername);
        options.setMavenRepoPassword(mavenRepoPassword);
        options.setApiFilename(apiFilename);
        options.setCurrentArtifactVersion(apiFilename);

        File mockDownloadedFile = mock(File.class);
        ApiFileResolverParameter apiFileResolverParameter = new ApiFileResolverParameter(mockDownloadedFile, apiFilename);

        File mockSwaggerFile = mock(File.class);
        String swaggerFilePath = "absoluteSwaggerFilePath";
        LatestArtifactDownloader downloader = mock(LatestArtifactDownloader.class);
        given(downloadOptionsFactory.create(options)).willReturn(downloadOptions);
        given(downloaderFactory.create(options)).willReturn(downloader);
        given(downloader.download(downloadOptions)).willReturn(mockDownloadedFile);
        given(swaggerFileResolver.resolve(apiFileResolverParameter)).willReturn(mockSwaggerFile);
        given(mockSwaggerFile.getAbsolutePath()).willReturn(swaggerFilePath);
        // when
        underTest.handle(options);
        // then
        assertThat(options).extracting(Options::getOldApiPath).isEqualTo(swaggerFilePath);
    }

    @Test
    void testHandleShouldNotDoAnythingIfLatestArtifactDownloadingIsNotEnabled() {
        // given
        String groupId = "groupId";
        String artifactId = "artifactId";

        Options options = new Options();
        options.setNewApiPath("newApi");
        options.setGroupId(groupId);
        options.setArtifactId(artifactId);
        // when
        underTest.handle(options);
        // then nothing happens
    }

    @Test
    void testHandleShouldIgnoreMavenSettingsWhenOldApiIsProvided() {
        // given
        Options options = new Options();
        options.setNewApiPath("newApi");
        options.setOldApiPath("oldApi");
        options.setMavenRepoUrl("mavenRepoUrl");
        options.setGroupId("groupId");
        options.setArtifactId("artifactId");
        options.setCurrentArtifactVersion("currentVersion");

        // when
        underTest.handle(options);
        // then
        verifyNoMoreInteractions(downloaderFactory, swaggerFileResolver);
    }
}
