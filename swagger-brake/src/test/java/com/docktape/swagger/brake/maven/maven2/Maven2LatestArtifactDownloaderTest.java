package com.docktape.swagger.brake.maven.maven2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.File;

import com.docktape.swagger.brake.maven.DownloadOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class Maven2LatestArtifactDownloaderTest {
    @Mock
    private LatestArtifactVersionResolver latestArtifactVersionResolver;

    @Mock
    private LatestArtifactNameResolver latestArtifactNameResolver;

    @Mock
    private LatestJarArtifactDownloader latestJarArtifactDownloader;

    @InjectMocks
    private Maven2LatestArtifactDownloader underTest;

    @Test
    void testDownloadWorksCorrectlyForSnapshot() {
        // given
        String latestVersion = "1.0.0-SNAPSHOT";
        String latestArtifactName = "b";
        File expectedFile = mock(File.class);
        DownloadOptions downloadOptions = mock(DownloadOptions.class);

        given(latestArtifactVersionResolver.resolve(downloadOptions)).willReturn(latestVersion);
        given(latestArtifactNameResolver.resolveSnapshot(downloadOptions, latestVersion)).willReturn(latestArtifactName);
        given(latestJarArtifactDownloader.download(downloadOptions, latestArtifactName, latestVersion)).willReturn(expectedFile);
        // when
        File result = underTest.download(downloadOptions);
        // then
        assertThat(result).isEqualTo(expectedFile);
    }

    @Test
    void testDownloadWorksCorrectlyForRelease() {
        // given
        String latestVersion = "1.0.0";
        String latestArtifactName = "b";
        File expectedFile = mock(File.class);
        DownloadOptions downloadOptions = mock(DownloadOptions.class);

        given(latestArtifactVersionResolver.resolve(downloadOptions)).willReturn(latestVersion);
        given(latestArtifactNameResolver.resolveRelease(downloadOptions, latestVersion)).willReturn(latestArtifactName);
        given(latestJarArtifactDownloader.download(downloadOptions, latestArtifactName, latestVersion)).willReturn(expectedFile);
        // when
        File result = underTest.download(downloadOptions);
        // then
        assertThat(result).isEqualTo(expectedFile);
    }
}