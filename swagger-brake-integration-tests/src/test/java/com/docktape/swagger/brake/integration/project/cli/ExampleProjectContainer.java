package com.docktape.swagger.brake.integration.project.cli;

import com.docktape.swagger.brake.integration.project.ProjectContainerBase;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.ImageFromDockerfile;

@Slf4j
public class ExampleProjectContainer extends ProjectContainerBase<CliProjectParameter> {
    public ExampleProjectContainer(Network network, CliProjectParameter param) {
        super(network, param);
    }

    @Override
    protected ImageFromDockerfile containerImage(CliProjectParameter parameter) {
        return new ImageFromDockerfile()
            .withFileFromClasspath("/", "/example-projects/swagger-brake-example")
            .withFileFromClasspath("/src/main/resources/swagger.yaml", parameter.getClasspathSwagger());
    }

    @Override
    protected GenericContainer configureContainer(GenericContainer container, CliProjectParameter parameter) {
        return container
            .withEnv("GROUP_ID", parameter.getGroupId())
            .withEnv("ARTIFACT_ID", parameter.getArtifactId());
    }

    @Override
    protected boolean isBuildSuccess(String logs) {
        return logs.contains("[INFO] BUILD SUCCESS");
    }

    @Override
    protected boolean isBuildFailure(String logs) {
        return logs.contains("[INFO] BUILD FAILURE") || logs.contains("[ERROR] To see the full stack trace of the errors");
    }
}
