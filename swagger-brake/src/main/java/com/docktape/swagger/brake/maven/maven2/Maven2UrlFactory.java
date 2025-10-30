package com.docktape.swagger.brake.maven.maven2;

import static java.lang.String.format;

import com.docktape.swagger.brake.maven.DownloadOptions;
import org.springframework.stereotype.Component;

@Component
public class Maven2UrlFactory {
    /**
     * Constructs the latest SNAPSHOT metadata URL.
     * @param options the {@link DownloadOptions}.
     * @param latestVersion the latest version available in the repo.
     * @return the URL string.
     */
    public String createLatestArtifactSnapshotMetadataUrl(DownloadOptions options, String latestVersion) {
        String artifactBasePathUrl = createLatestArtifactBasePathUrl(options, options.getSnapshotRepoUrl());
        return format("%s/%s/maven-metadata.xml", artifactBasePathUrl, latestVersion);
    }

    /**
     * Constructs the latest release metadata URL.
     * @param options the {@link DownloadOptions}.
     * @return the URL string.
     */
    public String createLatestArtifactVersionMetadataUrl(DownloadOptions options) {
        String repoUrl = getRepoUrl(options);
        String artifactBasePathUrl = createLatestArtifactBasePathUrl(options, repoUrl);
        return format("%s/maven-metadata.xml", artifactBasePathUrl);
    }

    /**
     * Constructs the latest JAR artifact URL.
     * @param options the {@link DownloadOptions}.
     * @param latestFilename the latest filename available.
     * @param latestVersion the latest version of the JAR.
     * @return the URL string.
     */
    public String createLatestArtifactUrl(DownloadOptions options, String latestFilename, String latestVersion) {
        String repoUrl = getRepoUrl(options);
        String artifactBasePathUrl = createLatestArtifactBasePathUrl(options, repoUrl);
        return format("%s/%s/%s.%s", artifactBasePathUrl, latestVersion, latestFilename, options.getArtifactPackaging().getPackaging());
    }

    private String createLatestArtifactBasePathUrl(DownloadOptions options, String repoUrl) {
        String groupPath = options.getGroupId().replaceAll("\\.", "/");
        return format("%s/%s/%s", repoUrl, groupPath, options.getArtifactId());
    }

    private String getRepoUrl(DownloadOptions options) {
        String repoUrl = options.getRepoUrl();
        if (ArtifactVersionDecider.isSnapshot(options.getCurrentArtifactVersion())) {
            repoUrl = options.getSnapshotRepoUrl();
        }
        return repoUrl;
    }
}
