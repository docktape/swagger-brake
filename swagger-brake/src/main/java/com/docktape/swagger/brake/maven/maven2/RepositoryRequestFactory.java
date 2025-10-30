package com.docktape.swagger.brake.maven.maven2;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.http.HttpRequestFactory;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepositoryRequestFactory {
    private final HttpRequestFactory requestFactory;

    /**
     * Creates an {@link HttpUriRequest} based on the parameters given. Either a Basic authorized one or
     * without authorization.
     * @param url the URL.
     * @param options the {@link DownloadOptions}.
     * @return the {@link HttpUriRequest}.
     */
    public HttpUriRequest create(String url, DownloadOptions options) {
        try {
            if (options.isAuthenticationNeeded()) {
                return requestFactory.authenticatedGet(url, options.getUsername(), options.getPassword());
            } else {
                return requestFactory.get(url);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while creating the http request", e);
        }
    }
}
