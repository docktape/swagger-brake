package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.docktape.swagger.brake.runner.ArtifactPackaging;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

public class ArtifactPackagingHandlerTest {
    private ArtifactPackagingHandler underTest = new ArtifactPackagingHandler();

    @Test
    public void testHandleWorksWhenJarGiven() {
        // given
        String propertyValue = "jar";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.JAR);
    }

    @Test
    public void testHandleWorksWhenWarGiven() {
        // given
        String propertyValue = "war";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.WAR);
    }

    @Test
    public void testHandleWorksWhenNullGiven() {
        // given
        String propertyValue = null;
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.JAR);
    }

    @Test
    public void testHandleWorksWhenEmptyGiven() {
        // given
        String propertyValue = "";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.JAR);
    }

    @Test
    public void testHandleWorksWhenBlankGiven() {
        // given
        String propertyValue = "";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.JAR);
    }

    @Test
    public void testHandleThrowsExceptionWhenUnsupportedPackagingIsProvided() {
        // given
        String propertyValue = "something";
        Options options = new Options();
        // when
        IllegalArgumentException iae = catchThrowableOfType(() -> underTest.handle(propertyValue, options), IllegalArgumentException.class);
        // then
        assertThat(iae.getMessage()).contains("not supported");
    }

}