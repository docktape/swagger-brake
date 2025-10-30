package com.docktape.swagger.brake.integration.project.plugin;

import com.docktape.swagger.brake.integration.project.ProjectContainerBase;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.ImageFromDockerfile;

@Slf4j
public abstract class PluginProjectContainerBase<T extends PluginProjectParameter> extends ProjectContainerBase<T> {
    protected PluginProjectContainerBase(Network network, T parameter) {
        super(network, parameter);
    }

    @Override
    protected ImageFromDockerfile containerImage(PluginProjectParameter parameter) {
        return new ImageFromDockerfile()
            .withFileFromClasspath("/", getProjectResourcePath())
            .withFileFromClasspath("/src/main/resources/swagger.yaml", parameter.getClasspathSwagger());
    }

    @Override
    protected GenericContainer configureContainer(GenericContainer container, PluginProjectParameter parameter) {
        return container
            .withEnv("SWAGGER_BRAKE_VERSION", parameter.getSwaggerBrakeVersion().getVersion())
            .withEnv("ARTIFACT_ID", parameter.getArtifactId());
    }

    protected abstract String getProjectResourcePath();
}
