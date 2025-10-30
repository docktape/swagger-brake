package com.docktape.swagger.brake.maven.http;

import static java.lang.String.format;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestFactory {
    /**
     * Prepares an {@link HttpGet} object with the specified parameters including an Authorization header. <br><br>
     * It converts the username and password parameters into a Basic authorization header.
     * @param url the URL of the request.
     * @param username the username for the API call.
     * @param password the password for the API call.
     * @return the {@link HttpGet} instance.
     * @throws MalformedURLException if no protocol is specified, or an unknown protocol is found, or spec is null.
     * @throws URISyntaxException if this URL is not formatted strictly according to to RFC2396 and cannot be converted to a URI.
     */
    public HttpGet authenticatedGet(String url, String username, String password) throws MalformedURLException, URISyntaxException {
        HttpGet result = get(url);
        result.addHeader(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderValue(username, password));
        return result;
    }

    public HttpGet get(String url) throws URISyntaxException, MalformedURLException {
        return new HttpGet(new URL(url).toURI());
    }

    private String getBasicAuthHeaderValue(String username, String password) {
        String auth = format("%s:%s", username, password);
        String encodedBasicAuth = Base64Utils.encode(auth);
        return format("Basic %s", encodedBasicAuth);
    }
}
