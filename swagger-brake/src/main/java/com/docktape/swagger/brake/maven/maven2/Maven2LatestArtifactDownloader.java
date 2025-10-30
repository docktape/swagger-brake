package com.docktape.swagger.brake.maven.maven2;

import java.io.File;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.LatestArtifactDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("maven2LatestArtifactDownloader")
@RequiredArgsConstructor
class Maven2LatestArtifactDownloader implements LatestArtifactDownloader {
    private final LatestArtifactVersionResolver latestArtifactVersionResolver;
    private final LatestArtifactNameResolver latestArtifactNameResolver;
    private final LatestJarArtifactDownloader latestJarArtifactDownloader;

    @Override
    public File download(DownloadOptions options) {
        String latestVersion = latestArtifactVersionResolver.resolve(options);
        String latestFilename;
        if (ArtifactVersionDecider.isSnapshot(latestVersion)) {
            latestFilename = latestArtifactNameResolver.resolveSnapshot(options, latestVersion);
        } else {
            latestFilename = latestArtifactNameResolver.resolveRelease(options, latestVersion);
        }
        return latestJarArtifactDownloader.download(options, latestFilename, latestVersion);
    }
}
