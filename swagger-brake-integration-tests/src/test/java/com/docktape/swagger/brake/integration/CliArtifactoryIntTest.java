package com.docktape.swagger.brake.integration;

import static com.docktape.swagger.brake.integration.support.SwaggerExamples.EXAMPLE_SWAGGER_1;
import static com.docktape.swagger.brake.integration.support.SwaggerExamples.EXAMPLE_SWAGGER_2;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.cli.SwaggerBrakeMain;
import com.docktape.swagger.brake.integration.artifactory.ArtifactoryServer;
import com.docktape.swagger.brake.integration.artifactory.ArtifactoryServerPool;
import com.docktape.swagger.brake.integration.docker.NetworkHolder;
import com.docktape.swagger.brake.integration.project.PackagingType;
import com.docktape.swagger.brake.integration.project.cli.CliProjectParameter;
import com.docktape.swagger.brake.integration.project.cli.ExampleProjectContainer;
import com.docktape.swagger.brake.integration.support.ArtifactIdGenerator;
import com.docktape.swagger.brake.integration.support.ClasspathFileLoader;
import org.testcontainers.containers.Network;
import org.testng.annotations.Test;

public class CliArtifactoryIntTest {
    public static final String GROUP_ID = "com.docktape.example";

    private final Network commonNetwork = NetworkHolder.getInstance();
    private final ArtifactoryServerPool artifactoryServerPool = ArtifactoryServerPool.getInstance();

    @Test
    public void testArtifactoryDownloadWorksWithCliForSnapshot() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        String version = "0.0.1-SNAPSHOT";
        CliProjectParameter parameter = createProjectParameter(artifactoryServer, artifactId, version, EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-repo-url=%s", artifactoryServer.getReleaseRepoUrl()),
            format("--maven-snapshot-repo-url=%s", artifactoryServer.getSnapshotRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", version),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId)
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(0);
    }

    @Test
    public void testArtifactoryDownloadWorksWithCliForSnapshotWithoutReleaseRepo() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        String version = "0.0.1-SNAPSHOT";
        CliProjectParameter parameter = createProjectParameter(artifactoryServer, artifactId, version, EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-snapshot-repo-url=%s", artifactoryServer.getSnapshotRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", version),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId)
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(0);
    }

    /**
     * The test case deploys 2 snapshot artifacts to Artifactory. Compared to the first Swagger file, the second one has
     * an additional API. <br>
     * Swagger Brake is executed with the first Swagger file being the "new one" so a breaking change shall be detected.
     * <br>
     * Also, this way, the latest snapshot resolution is also verified because if breaking changes are found,
     * Swagger Brake has downloaded the second artifact with the additional API.
     */
    @Test
    public void testArtifactoryDownloadWorksWithCliForSnapshots() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        String version = "0.0.2-SNAPSHOT";
        CliProjectParameter parameter = createProjectParameter(artifactoryServer, artifactId, version, EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();
        CliProjectParameter parameter2 = createProjectParameter(artifactoryServer, artifactId, version, EXAMPLE_SWAGGER_2);
        ExampleProjectContainer exampleProjectContainer2 = new ExampleProjectContainer(commonNetwork, parameter2);
        exampleProjectContainer2.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-repo-url=%s", artifactoryServer.getReleaseRepoUrl()),
            format("--maven-snapshot-repo-url=%s", artifactoryServer.getSnapshotRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", version),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId)
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(1);
    }

    /**
     * This test case is similar to {@link CliArtifactoryIntTest#testArtifactoryDownloadWorksWithCliForSnapshots}
     * except it is executing against 2 different versions.
     */
    @Test
    public void testArtifactoryDownloadWorksWithCliForDifferentVersionSnapshots() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        CliProjectParameter parameter = createProjectParameter(artifactoryServer, artifactId, "1.0.0-SNAPSHOT", EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();
        CliProjectParameter parameter2 = createProjectParameter(artifactoryServer, artifactId, "1.1.0-SNAPSHOT", EXAMPLE_SWAGGER_2);
        ExampleProjectContainer exampleProjectContainer2 = new ExampleProjectContainer(commonNetwork, parameter2);
        exampleProjectContainer2.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-repo-url=%s", artifactoryServer.getReleaseRepoUrl()),
            format("--maven-snapshot-repo-url=%s", artifactoryServer.getSnapshotRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", "1.0.0-SNAPSHOT"),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId)
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(1);
    }

    @Test
    public void testArtifactoryDownloadWorksWithCliForSingleRelease() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        String version = "1.0.0";
        CliProjectParameter parameter = createProjectParameter(artifactoryServer, artifactId, version, EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-repo-url=%s", artifactoryServer.getReleaseRepoUrl()),
            format("--maven-snapshot-repo-url=%s", artifactoryServer.getSnapshotRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", version),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId)
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(0);
    }

    @Test
    public void testArtifactoryDownloadWorksWithCliForSingleReleaseWithWarPackaging() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        String version = "1.0.0";
        CliProjectParameter parameter = createWarProjectParameter(artifactoryServer, artifactId, version, EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-repo-url=%s", artifactoryServer.getReleaseRepoUrl()),
            format("--maven-snapshot-repo-url=%s", artifactoryServer.getSnapshotRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", version),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId),
            format("--artifact-packaging=%s", "war")
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(0);
    }

    @Test
    public void testArtifactoryDownloadWorksWithCliForSingleReleaseWithoutSnapshotRepo() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        String version = "1.0.0";
        CliProjectParameter parameter = createProjectParameter(artifactoryServer, artifactId, version, EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-repo-url=%s", artifactoryServer.getReleaseRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", version),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId)
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(0);
    }


    /**
     * This test case is similar to {@link CliArtifactoryIntTest#testArtifactoryDownloadWorksWithCliForSnapshots}
     * except it is using release artifacts.
     */
    @Test
    public void testArtifactoryDownloadWorksWithCliForDifferentVersionReleases() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        CliProjectParameter parameter = createProjectParameter(artifactoryServer, artifactId, "1.0.0", EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();
        CliProjectParameter parameter2 = createProjectParameter(artifactoryServer, artifactId, "1.1.0", EXAMPLE_SWAGGER_2);
        ExampleProjectContainer exampleProjectContainer2 = new ExampleProjectContainer(commonNetwork, parameter2);
        exampleProjectContainer2.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-repo-url=%s", artifactoryServer.getReleaseRepoUrl()),
            format("--maven-snapshot-repo-url=%s", artifactoryServer.getSnapshotRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", "1.2.0"),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId),
            format("--artifact-packaging=%s", "jar")
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(1);
    }

    @Test
    public void testArtifactoryDownloadWorksWithCliForDifferentVersionReleasesAndWithWarPackaging() {
        // given
        ArtifactoryServer artifactoryServer = artifactoryServerPool.provide(commonNetwork);
        String artifactId = ArtifactIdGenerator.generate();
        CliProjectParameter parameter = createWarProjectParameter(artifactoryServer, artifactId, "1.0.0", EXAMPLE_SWAGGER_1);
        ExampleProjectContainer exampleProjectContainer = new ExampleProjectContainer(commonNetwork, parameter);
        exampleProjectContainer.build();
        CliProjectParameter parameter2 = createWarProjectParameter(artifactoryServer, artifactId, "1.1.0", EXAMPLE_SWAGGER_2);
        ExampleProjectContainer exampleProjectContainer2 = new ExampleProjectContainer(commonNetwork, parameter2);
        exampleProjectContainer2.build();

        String swaggerFilePath = ClasspathFileLoader.getAbsolutePath(EXAMPLE_SWAGGER_1);
        String[] commandLineArguments = asList(
            format("--new-api=%s", swaggerFilePath),
            format("--maven-repo-url=%s", artifactoryServer.getReleaseRepoUrl()),
            format("--maven-snapshot-repo-url=%s", artifactoryServer.getSnapshotRepoUrl()),
            format("--maven-repo-username=%s", ArtifactoryServer.DEFAULT_USERNAME),
            format("--maven-repo-password=%s", ArtifactoryServer.UPDATED_PASSWORD),
            format("--current-artifact-version=%s", "1.2.0"),
            format("--groupId=%s", GROUP_ID),
            format("--artifactId=%s", artifactId),
            format("--artifact-packaging=%s", "war")
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(1);
    }

    private CliProjectParameter createWarProjectParameter(ArtifactoryServer artifactoryServer, String artifactId, String version, String classpathSwagger) {
        CliProjectParameter result = createProjectParameter(artifactoryServer, artifactId, version, classpathSwagger);
        result.setPackagingType(PackagingType.WAR);
        return result;
    }

    private CliProjectParameter createProjectParameter(ArtifactoryServer artifactoryServer, String artifactId, String version, String classpathSwagger) {
        CliProjectParameter parameter = new CliProjectParameter();
        parameter.setGroupId(GROUP_ID);
        parameter.setArtifactId(artifactId);
        parameter.setPackagingType(PackagingType.JAR);
        parameter.setCustomVersion(version);
        parameter.setArtifactoryContextUrl(artifactoryServer.getBaseApiInternalUrl());
        parameter.setUsername(ArtifactoryServer.DEFAULT_USERNAME);
        parameter.setPassword(ArtifactoryServer.UPDATED_PASSWORD);
        parameter.setReleaseRepoKey(ArtifactoryServer.RELEASE_REPO_KEY);
        parameter.setReleaseRepoUrl(artifactoryServer.getInternalReleaseRepoUrl());
        parameter.setSnapshotRepoKey(ArtifactoryServer.SNAPSHOT_REPO_KEY);
        parameter.setSnapshotRepoUrl(artifactoryServer.getInternalSnapshotRepoUrl());
        parameter.setClasspathSwagger(classpathSwagger);
        return parameter;
    }
}
