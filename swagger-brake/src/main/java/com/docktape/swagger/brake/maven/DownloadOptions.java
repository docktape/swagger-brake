package com.docktape.swagger.brake.maven;

import com.docktape.swagger.brake.runner.ArtifactPackaging;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DownloadOptions {
    private String repoUrl;
    private String snapshotRepoUrl;
    private String groupId;
    private String artifactId;
    private String username;
    private String password;
    private String currentArtifactVersion;
    private ArtifactPackaging artifactPackaging;

    public boolean isAuthenticationNeeded() {
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
    }
}
