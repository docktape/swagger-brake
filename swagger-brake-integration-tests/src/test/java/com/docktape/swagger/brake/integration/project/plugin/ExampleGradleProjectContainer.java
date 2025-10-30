package com.docktape.swagger.brake.integration.project.plugin;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.Network;

@Slf4j
public class ExampleGradleProjectContainer extends PluginProjectContainerBase<PluginProjectParameter> {
    public ExampleGradleProjectContainer(Network network, PluginProjectParameter parameter) {
        super(network, parameter);
    }

    @Override
    protected String getProjectResourcePath() {
        return "/example-projects/swagger-brake-gradle-example";
    }

    @Override
    protected boolean isBuildSuccess(String logs) {
        return logs.contains("BUILD SUCCESS");
    }

    @Override
    protected boolean isBuildFailure(String logs) {
        return logs.contains("BUILD FAILED");
    }

    public boolean isFirstVersionAssumed() {
        return container.getLogs().contains("Assuming this is the first version of the artifact, skipping check for breaking changes");
    }

    public boolean isProjectGroupIdUsed() {
        return !container.getLogs().contains("Default groupId based on the project group cannot be used for Swagger Brake. Consider setting it manually.");
    }

    public boolean isProjectArtifactIdUsed() {
        return !container.getLogs().contains("Default artifactId based on the project name cannot be used for Swagger Brake. Consider setting it manually.");
    }

    public boolean isProjectVersionUsed() {
        return !container.getLogs().contains("Default currentVersion based on the project version cannot be used for Swagger Brake. Consider setting it manually.");
    }

    public boolean isProjectBuildDirUsed() {
        return !container.getLogs().contains("Default outputFilePath based on the project buildDir cannot be used for Swagger Brake. Consider setting it manually.");
    }

}
