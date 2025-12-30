package com.docktape.swagger.brake.maven.maven2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.http.HttpRequestFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepositoryRequestFactoryTest {
    @Mock
    private HttpRequestFactory requestFactory;

    @InjectMocks
    private RepositoryRequestFactory underTest;

    @Test
    void testCreateShouldReturnAuthenticatedRequestWhenAuthenticationIsNeeded() throws MalformedURLException, URISyntaxException {
        // given
        String url = "url";
        String username = "username";
        String password = "password";

        DownloadOptions options = new DownloadOptions();
        options.setUsername(username);
        options.setPassword(password);

        HttpGet expected = mock(HttpGet.class);
        given(requestFactory.authenticatedGet(url, username, password)).willReturn(expected);

        // when
        HttpUriRequest result = underTest.create(url, options);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testCreateShouldReturnUnauthenticatedRequestWhenAuthenticationIsNeeded() throws MalformedURLException, URISyntaxException {
        // given
        String url = "url";

        DownloadOptions options = new DownloadOptions();

        HttpGet expected = mock(HttpGet.class);
        given(requestFactory.get(url)).willReturn(expected);

        // when
        HttpUriRequest result = underTest.create(url, options);
        // then
        assertThat(result).isEqualTo(expected);
    }


    @Test
    void testCreateShouldThrowExceptionWhenRequestCannotBeCreated() throws MalformedURLException, URISyntaxException {
        // given
        String url = "url";
        DownloadOptions options = new DownloadOptions();
        given(requestFactory.get(url)).willThrow(MalformedURLException.class);
        // when
        assertThatThrownBy(() -> underTest.create(url, options)).isExactlyInstanceOf(RuntimeException.class);
        // then exception thrown
    }
}