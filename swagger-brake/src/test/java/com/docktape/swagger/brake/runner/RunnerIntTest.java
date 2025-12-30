package com.docktape.swagger.brake.runner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.docktape.swagger.brake.core.ApiInfo;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.LatestArtifactDownloader;
import com.docktape.swagger.brake.maven.LatestArtifactDownloaderFactory;
import com.docktape.swagger.brake.maven.jar.ApiFileJarResolver;
import com.docktape.swagger.brake.maven.jar.ApiFileResolverParameter;
import com.docktape.swagger.brake.report.Reporter;
import com.docktape.swagger.brake.report.ReporterFactory;
import com.docktape.swagger.brake.runner.download.ArtifactDownloaderHandler;
import com.docktape.swagger.brake.runner.download.DownloadOptionsFactory;
import com.docktape.swagger.brake.runner.openapi.ApiInfoFactory;
import com.docktape.swagger.brake.runner.openapi.OpenApiFactory;
import io.swagger.v3.oas.models.OpenAPI;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
class RunnerIntTest {
    @Mock
    private ReporterFactory reporterFactory;

    @Mock
    private OpenApiFactory openApiFactory;

    @Mock
    private Checker checker;

    @Spy
    private CheckerOptionsFactory checkerOptionsFactory;

    @Mock
    private LatestArtifactDownloaderFactory downloaderFactory;

    @Mock
    private ApiFileJarResolver apiFileResolver;

    @Mock
    private DownloadOptionsFactory downloadOptionsFactory;

    @Spy
    private ApiInfoFactory apiInfoFactory;

    private ArtifactDownloaderHandler artifactDownloaderHandler;

    @Spy
    private OptionsValidator optionsValidator;

    private Runner underTest;

    @BeforeEach
    void setUp() {
        artifactDownloaderHandler = new ArtifactDownloaderHandler(downloaderFactory, apiFileResolver, downloadOptionsFactory);
        underTest = new Runner(optionsValidator, artifactDownloaderHandler, openApiFactory, checkerOptionsFactory, checker, reporterFactory, apiInfoFactory);
    }

    @Test
    void testOptionValidationWorksWhenMavenConfigurationIsSetWithoutOneRepo() {
        // given
        String oldApiPath = "oldApiPath";
        Options options = new Options();
        options.setNewApiPath("something");
        options.setMavenSnapshotRepoUrl("localhost:8080/repo");
        options.setCurrentArtifactVersion("1.0.0-SNAPSHOT");
        options.setGroupId("com.docktape");
        options.setArtifactId("swagger-brake");
        DownloadOptions downloadOptions = mock(DownloadOptions.class);
        given(downloadOptionsFactory.create(options)).willReturn(downloadOptions);
        LatestArtifactDownloader latestArtifactDownloader = mock(LatestArtifactDownloader.class);
        given(downloaderFactory.create(options)).willReturn(latestArtifactDownloader);
        File apiJar = mock(File.class);
        given(latestArtifactDownloader.download(downloadOptions)).willReturn(apiJar);
        File swaggerFile = mock(File.class);
        given(apiFileResolver.resolve(any(ApiFileResolverParameter.class))).willReturn(swaggerFile);
        given(swaggerFile.getAbsolutePath()).willReturn(oldApiPath);
        OpenAPI oldApi = mock(OpenAPI.class);
        OpenAPI newApi = mock(OpenAPI.class);
        given(openApiFactory.fromFile(oldApiPath)).willReturn(oldApi);
        given(openApiFactory.fromFile(options.getNewApiPath())).willReturn(newApi);
        given(checker.check(eq(oldApi), eq(newApi), any(CheckerOptions.class))).willReturn(Collections.emptyList());
        Reporter reporter = mock(Reporter.class);
        given(reporterFactory.create(options)).willReturn(reporter);
        // when
        Collection<BreakingChange> result = underTest.run(options);
        // then
        assertThat(result).isEmpty();
        verify(downloadOptionsFactory).create(options);
        verify(downloaderFactory).create(options);
        verify(latestArtifactDownloader).download(downloadOptions);
        verify(apiFileResolver).resolve(any(ApiFileResolverParameter.class));
        verify(openApiFactory).fromFile(oldApiPath);
        verify(openApiFactory).fromFile(options.getNewApiPath());
        verify(checker).check(eq(oldApi), eq(newApi), any(CheckerOptions.class));
        verify(reporter).report(anyList(), eq(options), any(ApiInfo.class));
    }

    @Test
    void testOptionValidationWorksWhenMavenConfigurationIsSetWithAllRepos() {
        // given
        String oldApiPath = "oldApiPath";
        Options options = new Options();
        options.setNewApiPath("something");
        options.setMavenSnapshotRepoUrl("localhost:8080/snapshot-repo");
        options.setMavenRepoUrl("localhost:8080/repo");
        options.setCurrentArtifactVersion("1.0.0-SNAPSHOT");
        options.setGroupId("com.docktape");
        options.setArtifactId("swagger-brake");
        DownloadOptions downloadOptions = mock(DownloadOptions.class);
        given(downloadOptionsFactory.create(options)).willReturn(downloadOptions);
        LatestArtifactDownloader latestArtifactDownloader = mock(LatestArtifactDownloader.class);
        given(downloaderFactory.create(options)).willReturn(latestArtifactDownloader);
        File apiJar = mock(File.class);
        given(latestArtifactDownloader.download(downloadOptions)).willReturn(apiJar);
        File swaggerFile = mock(File.class);
        given(apiFileResolver.resolve(any(ApiFileResolverParameter.class))).willReturn(swaggerFile);
        given(swaggerFile.getAbsolutePath()).willReturn(oldApiPath);
        OpenAPI oldApi = mock(OpenAPI.class);
        OpenAPI newApi = mock(OpenAPI.class);
        given(openApiFactory.fromFile(oldApiPath)).willReturn(oldApi);
        given(openApiFactory.fromFile(options.getNewApiPath())).willReturn(newApi);
        given(checker.check(eq(oldApi), eq(newApi), any(CheckerOptions.class))).willReturn(Collections.emptyList());
        Reporter reporter = mock(Reporter.class);
        given(reporterFactory.create(options)).willReturn(reporter);
        // when
        Collection<BreakingChange> result = underTest.run(options);
        // then
        assertThat(result).isEmpty();
        verify(downloadOptionsFactory).create(options);
        verify(downloaderFactory).create(options);
        verify(latestArtifactDownloader).download(downloadOptions);
        verify(apiFileResolver).resolve(any(ApiFileResolverParameter.class));
        verify(openApiFactory).fromFile(oldApiPath);
        verify(openApiFactory).fromFile(options.getNewApiPath());
        verify(checker).check(eq(oldApi), eq(newApi), any(CheckerOptions.class));
        verify(reporter).report(anyList(), eq(options), any(ApiInfo.class));
    }
}
