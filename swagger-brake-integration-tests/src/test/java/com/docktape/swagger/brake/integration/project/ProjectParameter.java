package com.docktape.swagger.brake.integration.project;

import lombok.Data;

@Data
public class ProjectParameter {
    private String customVersion;
    private String artifactoryContextUrl;
    private String releaseRepoKey;
    private String releaseRepoUrl;
    private String snapshotRepoKey;
    private String snapshotRepoUrl;
    private String username;
    private String password;
    private String classpathSwagger;
    private PackagingType packagingType;
}
