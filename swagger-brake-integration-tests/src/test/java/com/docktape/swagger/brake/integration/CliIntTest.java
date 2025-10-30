package com.docktape.swagger.brake.integration;

import static com.docktape.swagger.brake.integration.support.SwaggerExamples.EXAMPLE_SWAGGER_1;
import static com.docktape.swagger.brake.integration.support.SwaggerExamples.EXAMPLE_SWAGGER_2;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import com.docktape.swagger.brake.cli.SwaggerBrakeMain;
import org.testng.annotations.Test;

public class CliIntTest {
    @Test
    public void testStandardCliReturnsZeroExitCodeIfNoBreakingChange() throws Exception {
        // given
        String swaggerFile1Path = new File(
            CliArtifactoryIntTest.class.getClassLoader()
                .getResource(EXAMPLE_SWAGGER_1).toURI())
            .getAbsolutePath();
        String[] commandLineArguments = asList(
            format("--old-api=%s", swaggerFile1Path),
            format("--new-api=%s", swaggerFile1Path)
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(0);
    }

    @Test
    public void testStandardCliDetectsBreakingChanges() throws Exception {
        // given
        String swaggerFile1Path = new File(
            CliArtifactoryIntTest.class.getClassLoader()
                .getResource(EXAMPLE_SWAGGER_1).toURI())
            .getAbsolutePath();
        String swaggerFile2Path = new File(
            CliArtifactoryIntTest.class.getClassLoader()
                .getResource(EXAMPLE_SWAGGER_2).toURI())
            .getAbsolutePath();
        String[] commandLineArguments = asList(
            format("--old-api=%s", swaggerFile2Path),
            format("--new-api=%s", swaggerFile1Path)
        ).toArray(new String[0]);
        // when
        int exitCode = SwaggerBrakeMain.createCliInterface(commandLineArguments).start();
        // then
        assertThat(exitCode).isEqualTo(1);
    }
}
