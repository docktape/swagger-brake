package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.docktape.swagger.brake.runner.ArtifactPackaging;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

class ArtifactPackagingHandlerTest {
    private final ArtifactPackagingHandler underTest = new ArtifactPackagingHandler();

    @Test
    void testHandleWorksWhenJarGiven() {
        // given
        String propertyValue = "jar";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.JAR);
    }

    @Test
    void testHandleWorksWhenWarGiven() {
        // given
        String propertyValue = "war";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.WAR);
    }

    @Test
    void testHandleWorksWhenNullGiven() {
        // given
        String propertyValue = null;
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.JAR);
    }

    @Test
    void testHandleWorksWhenEmptyGiven() {
        // given
        String propertyValue = "";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.JAR);
    }

    @Test
    void testHandleWorksWhenBlankGiven() {
        // given
        String propertyValue = "";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getArtifactPackaging).isEqualTo(ArtifactPackaging.JAR);
    }

    @Test
    void testHandleThrowsExceptionWhenUnsupportedPackagingIsProvided() {
        // given
        String propertyValue = "something";
        Options options = new Options();
        // when
        IllegalArgumentException iae = catchThrowableOfType(() -> underTest.handle(propertyValue, options), IllegalArgumentException.class);
        // then
        assertThat(iae.getMessage()).contains("not supported");
    }
}