package com.docktape.swagger.brake.maven.maven2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.model.MavenMetadata;
import com.docktape.swagger.brake.maven.model.MavenVersioning;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LatestArtifactVersionResolverTest {
    @Mock
    private Maven2UrlFactory urlFactory;

    @Mock
    private MavenMetadataDownloader metadataDownloader;

    @Mock
    private RepositoryRequestFactory requestFactory;

    @InjectMocks
    private LatestArtifactVersionResolver underTest;


    @Test
    public void testResolveShouldThrowExceptionWhenNoVersionCanBeResolved() {
        // given
        MavenMetadata mavenMetadata = new MavenMetadata("groupId", "artifactId", new MavenVersioning(null, null, null));

        String metadataUrl = "url";
        DownloadOptions options = mock(DownloadOptions.class);
        HttpUriRequest metadataRequest = mock(HttpUriRequest.class);
        given(urlFactory.createLatestArtifactVersionMetadataUrl(options)).willReturn(metadataUrl);
        given(requestFactory.create(metadataUrl, options)).willReturn(metadataRequest);
        given(metadataDownloader.download(metadataRequest)).willReturn(mavenMetadata);
        // when
        assertThatThrownBy(() -> underTest.resolve(options)).isExactlyInstanceOf(IllegalStateException.class);
        // then exception thrown
    }

    @Test
    public void testResolveShouldWorkProperlyForStandard() {
        // given
        String expected = "latest";
        MavenMetadata mavenMetadata = new MavenMetadata("groupId", "artifactId", new MavenVersioning(expected, null, null));

        String metadataUrl = "url";
        DownloadOptions options = mock(DownloadOptions.class);
        HttpUriRequest metadataRequest = mock(HttpUriRequest.class);
        given(urlFactory.createLatestArtifactVersionMetadataUrl(options)).willReturn(metadataUrl);
        given(requestFactory.create(metadataUrl, options)).willReturn(metadataRequest);
        given(metadataDownloader.download(metadataRequest)).willReturn(mavenMetadata);
        // when
        String result = underTest.resolve(options);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testResolveShouldWorkProperlyForNexus() {
        // given
        String expected = "latest";
        MavenMetadata mavenMetadata = new MavenMetadata("groupId", "artifactId", new MavenVersioning(null, expected, null));

        String metadataUrl = "url";
        DownloadOptions options = mock(DownloadOptions.class);
        HttpUriRequest metadataRequest = mock(HttpUriRequest.class);
        given(urlFactory.createLatestArtifactVersionMetadataUrl(options)).willReturn(metadataUrl);
        given(requestFactory.create(metadataUrl, options)).willReturn(metadataRequest);
        given(metadataDownloader.download(metadataRequest)).willReturn(mavenMetadata);
        // when
        String result = underTest.resolve(options);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testResolveShouldPreferStandardVersioningWhenStandardAndNexusSpecificIsPresent() {
        // given
        String expected = "latest";
        MavenMetadata mavenMetadata = new MavenMetadata("groupId", "artifactId", new MavenVersioning(expected, "nexus", null));

        String metadataUrl = "url";
        DownloadOptions options = mock(DownloadOptions.class);
        HttpUriRequest metadataRequest = mock(HttpUriRequest.class);
        given(urlFactory.createLatestArtifactVersionMetadataUrl(options)).willReturn(metadataUrl);
        given(requestFactory.create(metadataUrl, options)).willReturn(metadataRequest);
        given(metadataDownloader.download(metadataRequest)).willReturn(mavenMetadata);
        // when
        String result = underTest.resolve(options);
        // then
        assertThat(result).isEqualTo(expected);
    }
}