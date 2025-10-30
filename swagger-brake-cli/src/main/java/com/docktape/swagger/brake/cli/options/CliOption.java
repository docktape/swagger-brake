package com.docktape.swagger.brake.cli.options;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CliOption {
    OLD_API_PATH("old-api"),
    NEW_API_PATH("new-api"),
    HELP("help"),
    VERBOSE("verbose"),

    OUTPUT_FORMATS("output-formats"),
    OUTPUT_PATH("output-path"),

    MAVEN_REPO_URL("maven-repo-url"),
    MAVEN_SNAPSHOT_REPO_URL("maven-snapshot-repo-url"),
    MAVEN_REPO_USERNAME("maven-repo-username"),
    MAVEN_REPO_PASSWORD("maven-repo-password"),
    ARTIFACT_ID("artifactId"),
    GROUP_ID("groupId"),
    CURRENT_ARTIFACT_VERSION("current-artifact-version"),
    ARTIFACT_PACKAGING("artifact-packaging"),

    DEPRECATED_API_DELETION_ALLOWED("deprecated-api-deletion-allowed"),

    API_FILENAME("api-filename"),
    BETA_API_EXTENSION_NAME("beta-api-extension-name"),
    EXCLUDED_PATHS("excluded-paths"),
    IGNORED_BREAKING_CHANGE_RULES("ignored-breaking-change-rules");


    private final String cliOptionName;

    public String asName() {
        return cliOptionName;
    }

    public String asCliOption() {
        return "--" + cliOptionName;
    }
}
