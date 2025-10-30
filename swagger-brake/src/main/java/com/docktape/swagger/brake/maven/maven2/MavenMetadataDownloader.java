package com.docktape.swagger.brake.maven.maven2;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.docktape.swagger.brake.maven.model.MavenMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class MavenMetadataDownloader {
    private final HttpClient httpClient;
    private final XmlMapper xmlMapper;

    MavenMetadata download(HttpUriRequest httpRequest) {
        try {
            log.debug("Downloading maven metadata from {}", httpRequest.getURI());
            HttpResponse response = httpClient.execute(httpRequest);
            // TODO: content type check would be great here
            return getMetadata(response);
        } catch (IOException e) {
            throw new RuntimeException("Cannot get metadata", e);
        }
    }

    private MavenMetadata getMetadata(HttpResponse response) throws IOException {
        InputStream content = response.getEntity().getContent();
        return xmlMapper.readValue(content, MavenMetadata.class);
    }
}
