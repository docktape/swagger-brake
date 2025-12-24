package com.docktape.swagger.brake.maven;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DownloadOptionsTest {
    @Test
    void testIsAuthenticationNeededShouldReturnTrueWhenUsernameAndPasswordIsSet() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setUsername("username");
        options.setPassword("password");
        // when
        boolean result = options.isAuthenticationNeeded();
        // then
        assertThat(result).isTrue();
    }

    @Test
    void testIsAuthenticationNeededShouldReturnFalseWhenOnlyUsernameIsSet() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setUsername("username");
        // when
        boolean result = options.isAuthenticationNeeded();
        // then
        assertThat(result).isFalse();
    }

    @Test
    void testIsAuthenticationNeededShouldReturnFalseWhenOnlyUsernameIsSetAndPasswordIsBlank() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setUsername("username");
        options.setPassword("");
        // when
        boolean result = options.isAuthenticationNeeded();
        // then
        assertThat(result).isFalse();
    }

    @Test
    void testIsAuthenticationNeededShouldReturnFalseWhenOnlyPasswordIsSet() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setPassword("password");
        // when
        boolean result = options.isAuthenticationNeeded();
        // then
        assertThat(result).isFalse();
    }

    @Test
    void testIsAuthenticationNeededShouldReturnFalseWhenOnlyPasswordIsSetAndUsernameIsBlank() {
        // given
        DownloadOptions options = new DownloadOptions();
        options.setUsername("");
        options.setPassword("password");
        // when
        boolean result = options.isAuthenticationNeeded();
        // then
        assertThat(result).isFalse();
    }
}