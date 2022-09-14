package io.redskap.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import io.redskap.swagger.brake.cli.options.CliOption;
import io.redskap.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

public class CurrentArtifactVersionHandlerTest {
    private CurrentArtifactVersionHandler underTest = new CurrentArtifactVersionHandler();

    @Test
    public void testHandleWorks() {
        // given
        String propertyValue = "something";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getCurrentArtifactVersion).isEqualTo(propertyValue);
    }

    @Test
    public void testHandleDoesNotDoAnythingIfPropertyIsNull() {
        // given
        String propertyValue = null;
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getCurrentArtifactVersion).isNull();
    }

    @Test
    public void testHandleDoesNotDoAnythingIfPropertyIsEmpty() {
        // given
        String propertyValue = "";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getCurrentArtifactVersion).isNull();
    }

    @Test
    public void testHandleDoesNotDoAnythingIfPropertyIsBlank() {
        // given
        String propertyValue = "   ";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getCurrentArtifactVersion).isNull();
    }

    @Test
    public void testGetHandledCliOptionIsCorrect() {
        // given
        // when
        CliOption result = underTest.getHandledCliOption();
        // then
        assertThat(result).isEqualTo(CliOption.CURRENT_ARTIFACT_VERSION);
    }
}