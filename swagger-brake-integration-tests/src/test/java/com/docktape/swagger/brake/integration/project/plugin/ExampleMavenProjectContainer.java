package com.docktape.swagger.brake.integration.project.plugin;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

@Slf4j
public class ExampleMavenProjectContainer extends PluginProjectContainerBase<PluginProjectParameter> {
    public ExampleMavenProjectContainer(Network network, PluginProjectParameter parameter) {
        super(network, parameter);
    }

    @Override
    protected GenericContainer configureContainer(GenericContainer container, PluginProjectParameter parameter) {
        return super.configureContainer(container, parameter)
            .withEnv("REMOVE_PACKAGING", Boolean.toString(parameter.isRemovePackaging()));
    }

    @Override
    protected String getProjectResourcePath() {
        return "/example-projects/swagger-brake-maven-example";
    }

    @Override
    protected boolean isBuildSuccess(String logs) {
        return logs.contains("BUILD SUCCESS");
    }

    @Override
    protected boolean isBuildFailure(String logs) {
        return logs.contains("BUILD FAILURE");
    }

    public boolean isFirstVersionAssumed() {
        return container.getLogs().contains("Assuming this is the first version of the artifact, skipping check for breaking changes");
    }
}
