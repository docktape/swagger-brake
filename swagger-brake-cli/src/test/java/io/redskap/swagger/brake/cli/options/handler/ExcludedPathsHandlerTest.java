package io.redskap.swagger.brake.cli.options.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Joiner;
import io.redskap.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;

public class ExcludedPathsHandlerTest {
    private ExcludedPathsHandler underTest = new ExcludedPathsHandler();

    @Test
    public void testHandleShouldNotSetValueWhenNullIsPassed() {
        // given
        Options options = new Options();
        // when
        underTest.handle(null, options);
        // then
        assertThat(options.getExcludedPaths()).isEmpty();
    }

    @Test
    public void testHandleShouldNotSetValueWhenBlankIsPassed() {
        // given
        Options options = new Options();
        // when
        underTest.handle("   ", options);
        // then
        assertThat(options.getExcludedPaths()).isEmpty();
    }

    @Test
    public void testHandleShouldSetSingleValue() {
        // given
        Options options = new Options();
        // when
        String path1 = "/test/path";
        underTest.handle(path1, options);
        // then
        assertThat(options.getExcludedPaths()).containsExactlyInAnyOrder(path1);
    }

    @Test
    public void testHandleShouldSetMultiValue() {
        // given
        Options options = new Options();
        // when
        String path1 = "/test/path";
        String path2 = "/test2/path2";
        underTest.handle(Joiner.on(",").join(path1, path2), options);
        // then
        assertThat(options.getExcludedPaths()).containsExactlyInAnyOrder(path1, path2);
    }
}