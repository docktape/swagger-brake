package com.docktape.swagger.brake.maven.maven2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.docktape.swagger.brake.maven.DownloadOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class TemporaryJarFileDownloader {
    private final HttpClient httpClient;

    File download(DownloadOptions options, HttpUriRequest httpRequest) {
        try {
            log.debug("Downloading artifact from {}", httpRequest.getURI());
            File destination = Files.createTempFile("swagger-brake", "." + options.getArtifactPackaging().getPackaging()).toFile();
            HttpResponse response = httpClient.execute(httpRequest);
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(), destination);
            log.debug("Created temporary artifact file to {}", destination.getAbsolutePath());
            return destination;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
