package com.docktape.swagger.brake.maven.maven2;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.maven.model.MavenMetadata;
import com.docktape.swagger.brake.maven.model.MavenVersioning;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LatestArtifactVersionResolver {
    private final Maven2UrlFactory urlFactory;
    private final MavenMetadataDownloader metadataDownloader;
    private final RepositoryRequestFactory requestFactory;

    String resolve(DownloadOptions options) {
        String metadataUrl = urlFactory.createLatestArtifactVersionMetadataUrl(options);
        MavenMetadata mavenMetadata = metadataDownloader.download(requestFactory.create(metadataUrl, options));
        MavenVersioning versioning = mavenMetadata.getVersioning();
        String version = resolveStandardVersioning(versioning);
        if (version == null) {
            version = resolveNexusVersioning(versioning);
        }
        if (version == null) {
            throw new IllegalStateException("No version has been found for the metadata");
        }
        return version;
    }

    private String resolveStandardVersioning(MavenVersioning versioning) {
        if (versioning != null && versioning.getLatest() != null) {
            return versioning.getLatest();
        }
        return null;
    }

    private String resolveNexusVersioning(MavenVersioning versioning) {
        if (versioning != null && versioning.getRelease() != null) {
            return versioning.getRelease();
        }
        return null;
    }
}
