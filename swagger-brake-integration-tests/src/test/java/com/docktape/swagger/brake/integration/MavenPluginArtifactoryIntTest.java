package com.docktape.swagger.brake.integration;

import static com.docktape.swagger.brake.integration.support.SwaggerExamples.EXAMPLE_SWAGGER_1;
import static com.docktape.swagger.brake.integration.support.SwaggerExamples.EXAMPLE_SWAGGER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.docktape.swagger.brake.integration.artifactory.ArtifactoryServer;
import com.docktape.swagger.brake.integration.artifactory.ArtifactoryServerPool;
import com.docktape.swagger.brake.integration.docker.NetworkHolder;
import com.docktape.swagger.brake.integration.project.BuildFailureException;
import com.docktape.swagger.brake.integration.project.PackagingType;
import com.docktape.swagger.brake.integration.project.plugin.ExampleMavenProjectContainer;
import com.docktape.swagger.brake.integration.project.plugin.PluginProjectParameter;
import com.docktape.swagger.brake.integration.support.ArtifactIdGenerator;
import com.docktape.swagger.brake.integration.support.SwaggerBrakeVersion;
import org.testcontainers.containers.Network;
import org.testng.annotations.Test;

public class MavenPluginArtifactoryIntTest {
    private final Network commonNetwork = NetworkHolder.getInstance();
    private final ArtifactoryServerPool artifactoryServerPool = ArtifactoryServerPool.getInstance();

    @Test
    public void testArtifactoryDownloadWorksWithMavenForRelease() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        PluginProjectParameter projectParameter = createProjectParameter(artifactoryServer, artifactId, "1.0.0", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_1);
        ExampleMavenProjectContainer projectContainer = new ExampleMavenProjectContainer(commonNetwork, projectParameter);
        PluginProjectParameter projectParameter2 = createProjectParameter(artifactoryServer, artifactId, "1.1.0", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_1);
        ExampleMavenProjectContainer projectContainer2 = new ExampleMavenProjectContainer(commonNetwork, projectParameter2);
        // when
        projectContainer.build();
        assertThat(projectContainer.isFirstVersionAssumed()).isTrue();
        projectContainer2.build();
        // then no exception happens
        assertThat(projectContainer2.isFirstVersionAssumed()).isFalse();
        assertThat(projectContainer2.noBreakingApi()).isTrue();
    }

    @Test
    public void testArtifactoryDownloadWorksWithMavenForSnapshot() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        PluginProjectParameter projectParameter = createProjectParameter(artifactoryServer, artifactId, "1.0.0-SNAPSHOT", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_1);
        ExampleMavenProjectContainer projectContainer = new ExampleMavenProjectContainer(commonNetwork, projectParameter);
        PluginProjectParameter projectParameter2 = createProjectParameter(artifactoryServer, artifactId, "1.0.0-SNAPSHOT", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_1);
        ExampleMavenProjectContainer projectContainer2 = new ExampleMavenProjectContainer(commonNetwork, projectParameter2);
        // when
        projectContainer.build();
        assertThat(projectContainer.isFirstVersionAssumed()).isTrue();
        projectContainer2.build();
        // then no exception happens
        assertThat(projectContainer2.isFirstVersionAssumed()).isFalse();
        assertThat(projectContainer2.noBreakingApi()).isTrue();
    }

    @Test
    public void testArtifactoryDownloadReportsBreakingChangesWithMavenForRelease() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        PluginProjectParameter projectParameter = createProjectParameter(artifactoryServer, artifactId, "1.0.0", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_2);
        ExampleMavenProjectContainer projectContainer = new ExampleMavenProjectContainer(commonNetwork, projectParameter);
        PluginProjectParameter projectParameter2 = createProjectParameter(artifactoryServer, artifactId, "1.1.0", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_1);
        ExampleMavenProjectContainer projectContainer2 = new ExampleMavenProjectContainer(commonNetwork, projectParameter2);
        // when
        projectContainer.build();
        assertThat(projectContainer.isFirstVersionAssumed()).isTrue();
        Throwable exception = catchThrowable(projectContainer2::build);
        // then
        assertThat(exception).isInstanceOf(BuildFailureException.class);
        assertThat(projectContainer2.isFirstVersionAssumed()).isFalse();
        assertThat(projectContainer2.isApiBrokenWith("Path /pet/{petId} DELETE has been deleted")).isTrue();
    }

    @Test
    public void testArtifactoryDownloadReportsBreakingChangesWithMavenForReleaseWithRemovePackaging() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        PluginProjectParameter projectParameter = createProjectParameter(artifactoryServer, artifactId, "1.0.0", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_2);
        projectParameter.setRemovePackaging(true);
        ExampleMavenProjectContainer projectContainer = new ExampleMavenProjectContainer(commonNetwork, projectParameter);
        PluginProjectParameter projectParameter2 = createProjectParameter(artifactoryServer, artifactId, "1.1.0", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_1);
        projectParameter2.setRemovePackaging(true);
        ExampleMavenProjectContainer projectContainer2 = new ExampleMavenProjectContainer(commonNetwork, projectParameter2);
        // when
        projectContainer.build();
        assertThat(projectContainer.isFirstVersionAssumed()).isTrue();
        Throwable exception = catchThrowable(projectContainer2::build);
        // then
        assertThat(exception).isInstanceOf(BuildFailureException.class);
        assertThat(projectContainer2.isFirstVersionAssumed()).isFalse();
        assertThat(projectContainer2.isApiBrokenWith("Path /pet/{petId} DELETE has been deleted")).isTrue();
    }

    @Test
    public void testArtifactoryDownloadReportsBreakingChangesWithMavenForReleaseWithWarPackaging() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        PluginProjectParameter projectParameter = createWarProjectParameter(artifactoryServer, artifactId, "1.0.0", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_2);
        ExampleMavenProjectContainer projectContainer = new ExampleMavenProjectContainer(commonNetwork, projectParameter);
        PluginProjectParameter projectParameter2 = createWarProjectParameter(artifactoryServer, artifactId, "1.1.0", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_1);
        ExampleMavenProjectContainer projectContainer2 = new ExampleMavenProjectContainer(commonNetwork, projectParameter2);
        // when
        projectContainer.build();
        assertThat(projectContainer.isFirstVersionAssumed()).isTrue();
        Throwable exception = catchThrowable(projectContainer2::build);
        // then
        assertThat(exception).isInstanceOf(BuildFailureException.class);
        assertThat(projectContainer2.isFirstVersionAssumed()).isFalse();
        assertThat(projectContainer2.isApiBrokenWith("Path /pet/{petId} DELETE has been deleted")).isTrue();
    }

    @Test
    public void testArtifactoryDownloadReportsBreakingChangesWithMavenForSnapshot() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        PluginProjectParameter projectParameter = createProjectParameter(artifactoryServer, artifactId, "1.0.0-SNAPSHOT", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_2);
        ExampleMavenProjectContainer projectContainer = new ExampleMavenProjectContainer(commonNetwork, projectParameter);
        PluginProjectParameter projectParameter2 = createProjectParameter(artifactoryServer, artifactId, "1.0.0-SNAPSHOT", SwaggerBrakeVersion.V_LATEST_VERSION, EXAMPLE_SWAGGER_1);
        ExampleMavenProjectContainer projectContainer2 = new ExampleMavenProjectContainer(commonNetwork, projectParameter2);
        // when
        projectContainer.build();
        assertThat(projectContainer.isFirstVersionAssumed()).isTrue();
        Throwable exception = catchThrowable(projectContainer2::build);
        // then
        assertThat(exception).isInstanceOf(BuildFailureException.class);
        assertThat(projectContainer2.isFirstVersionAssumed()).isFalse();
        assertThat(projectContainer2.isApiBrokenWith("Path /pet/{petId} DELETE has been deleted")).isTrue();
    }

    private PluginProjectParameter createWarProjectParameter(ArtifactoryServer artifactoryServer, String artifactId, String customVersion, SwaggerBrakeVersion swaggerBrakeVersion,
                                                             String classpathSwagger) {
        PluginProjectParameter result = createProjectParameter(artifactoryServer, artifactId, customVersion, swaggerBrakeVersion, classpathSwagger);
        result.setPackagingType(PackagingType.WAR);
        return result;
    }

    private PluginProjectParameter createProjectParameter(ArtifactoryServer artifactoryServer, String artifactId, String customVersion, SwaggerBrakeVersion swaggerBrakeVersion,
                                                          String classpathSwagger) {
        PluginProjectParameter parameter = new PluginProjectParameter();
        parameter.setCustomVersion(customVersion);
        parameter.setArtifactId(artifactId);
        parameter.setPackagingType(PackagingType.JAR);
        if (artifactoryServer != null) {
            parameter.setArtifactoryContextUrl(artifactoryServer.getBaseApiInternalUrl());
            parameter.setReleaseRepoUrl(artifactoryServer.getInternalReleaseRepoUrl());
            parameter.setSnapshotRepoUrl(artifactoryServer.getInternalSnapshotRepoUrl());
        }
        parameter.setUsername(ArtifactoryServer.DEFAULT_USERNAME);
        parameter.setPassword(ArtifactoryServer.UPDATED_PASSWORD);
        parameter.setReleaseRepoKey(ArtifactoryServer.RELEASE_REPO_KEY);
        parameter.setSnapshotRepoKey(ArtifactoryServer.SNAPSHOT_REPO_KEY);
        parameter.setClasspathSwagger(classpathSwagger);
        parameter.setSwaggerBrakeVersion(swaggerBrakeVersion);
        return parameter;
    }
}
