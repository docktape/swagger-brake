package com.docktape.swagger.brake.maven.maven2;

import static java.lang.String.format;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.model.MavenMetadata;
import com.docktape.swagger.brake.maven.model.MavenSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LatestArtifactNameResolver {
    private final Maven2UrlFactory urlFactory;
    private final MavenMetadataDownloader metadataDownloader;
    private final RepositoryRequestFactory requestFactory;

    String resolveSnapshot(DownloadOptions options, String latestVersion) {
        String metadataUrl = urlFactory.createLatestArtifactSnapshotMetadataUrl(options, latestVersion);
        MavenMetadata snapshotMetadata = metadataDownloader.download(requestFactory.create(metadataUrl, options));
        MavenSnapshot snapshot = snapshotMetadata.getVersioning().getSnapshot();
        String snapshotVersion = latestVersion.replaceAll("SNAPSHOT", snapshot.getTimestamp());
        return format("%s-%s-%s", snapshotMetadata.getArtifactId(), snapshotVersion, snapshot.getBuildNumber());
    }

    String resolveRelease(DownloadOptions options, String latestVersion) {
        return format("%s-%s", options.getArtifactId(), latestVersion);
    }
}
