package com.docktape.swagger.brake.runner.download;

import com.docktape.swagger.brake.maven.DownloadOptions;
import com.docktape.swagger.brake.runner.Options;
import org.springframework.stereotype.Component;

@Component
public class DownloadOptionsFactory {
    /**
     * Creates a specific artifact download related option parameter.
     * @param options the {@link Options} instance to start from.
     * @return the {@link DownloadOptions} instance.
     */
    public DownloadOptions create(Options options) {
        DownloadOptions result = new DownloadOptions();
        result.setRepoUrl(options.getMavenRepoUrl());
        result.setSnapshotRepoUrl(options.getMavenSnapshotRepoUrl());
        result.setGroupId(options.getGroupId());
        result.setArtifactId(options.getArtifactId());
        result.setUsername(options.getMavenRepoUsername());
        result.setPassword(options.getMavenRepoPassword());
        result.setCurrentArtifactVersion(options.getCurrentArtifactVersion());
        result.setArtifactPackaging(options.getArtifactPackaging());
        return result;
    }
}
