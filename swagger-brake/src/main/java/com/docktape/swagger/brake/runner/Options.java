package com.docktape.swagger.brake.runner;

import java.util.Collections;
import java.util.Set;

import lombok.Data;

@Data
public class Options {
    private String oldApiPath;
    private String newApiPath;

    private Set<OutputFormat> outputFormats = Collections.emptySet();
    private String outputFilePath;

    private String mavenRepoUrl;
    private String mavenSnapshotRepoUrl;
    private String groupId;
    private String artifactId;
    private String mavenRepoUsername;
    private String mavenRepoPassword;
    private String currentArtifactVersion;
    private ArtifactPackaging artifactPackaging;

    private Boolean deprecatedApiDeletionAllowed;

    private String apiFilename;
    private String betaApiExtensionName;
    private Set<String> excludedPaths = Collections.emptySet();
    private Set<String> ignoredBreakingChangeRules = Collections.emptySet();
}
