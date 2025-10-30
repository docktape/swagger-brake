package com.docktape.swagger.brake.maven.maven2;

import java.io.File;

import com.docktape.swagger.brake.maven.DownloadOptions;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LatestJarArtifactDownloader {
    private final Maven2UrlFactory urlFactory;
    private final TemporaryJarFileDownloader temporaryJarFileDownloader;
    private final RepositoryRequestFactory requestFactory;

    File download(DownloadOptions options, String latestFilename, String latestVersion) {
        String url = urlFactory.createLatestArtifactUrl(options, latestFilename, latestVersion);
        HttpUriRequest httpRequest = requestFactory.create(url, options);
        return temporaryJarFileDownloader.download(options, httpRequest);
    }
}
