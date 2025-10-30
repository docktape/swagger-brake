package com.docktape.swagger.brake.maven.jar.filename;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class SwaggerApiFileNameCheckerTest {
    private SwaggerApiFileNameChecker underTest = new SwaggerApiFileNameChecker();

    @Test
    public void testIsApiFileReturnsFalseWhenNotSwagger() {
        // given
        // when
        boolean result = underTest.isApiFile("something.yaml");
        // then
        assertThat(result).isFalse();
    }

    @Test
    public void testIsApiFileReturnsTrueWhenSwaggerYaml() {
        // given
        // when
        boolean result = underTest.isApiFile("swagger.yaml");
        // then
        assertThat(result).isTrue();
    }

    @Test
    public void testIsApiFilenameReturnsTrueWhenSwaggerYml() {
        // given
        // when
        boolean result = underTest.isApiFile("swagger.yml");
        // then
        assertThat(result).isTrue();
    }

    @Test
    public void testIsApiFilenameReturnsTrueWhenSwaggerJson() {
        // given
        // when
        boolean result = underTest.isApiFile("swagger.json");
        // then
        assertThat(result).isTrue();
    }

    @Test
    public void testIsApiFileReturnsTrueWhenSwaggerYamlAndFullPath() {
        // given
        // when
        boolean result = underTest.isApiFile("c:/something/project/swagger.yaml");
        // then
        assertThat(result).isTrue();
    }
}