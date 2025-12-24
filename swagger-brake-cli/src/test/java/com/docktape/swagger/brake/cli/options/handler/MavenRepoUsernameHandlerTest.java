package com.docktape.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

class MavenRepoUsernameHandlerTest {
    private final MavenRepoUsernameHandler underTest = new MavenRepoUsernameHandler();

    @Test
    void testHandleWorks() {
        // given
        String propertyValue = "something";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getMavenRepoUsername).isEqualTo(propertyValue);
    }

    @Test
    void testHandleDoesNotDoAnythingIfPropertyIsNull() {
        // given
        String propertyValue = null;
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getMavenRepoUsername).isNull();
    }

    @Test
    void testHandleDoesNotDoAnythingIfPropertyIsEmpty() {
        // given
        String propertyValue = "";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getMavenRepoUsername).isNull();
    }

    @Test
    void testHandleDoesNotDoAnythingIfPropertyIsBlank() {
        // given
        String propertyValue = "   ";
        Options options = new Options();
        // when
        underTest.handle(propertyValue, options);
        // then
        assertThat(options).extracting(Options::getMavenRepoUsername).isNull();
    }

    @Test
    void testGetHandledCliOptionIsCorrect() {
        // given
        // when
        CliOption result = underTest.getHandledCliOption();
        // then
        assertThat(result).isEqualTo(CliOption.MAVEN_REPO_USERNAME);
    }
}