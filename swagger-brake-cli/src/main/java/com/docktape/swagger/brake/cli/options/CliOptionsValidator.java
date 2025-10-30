package com.docktape.swagger.brake.cli.options;

import static com.docktape.swagger.brake.cli.options.CliOption.*;

import com.docktape.swagger.brake.runner.OptionsValidator;
import org.springframework.stereotype.Component;

@Component
public class CliOptionsValidator extends OptionsValidator {
    @Override
    protected String getMavenRepoUrlName() {
        return MAVEN_REPO_URL.asCliOption();
    }

    @Override
    protected String getMavenSnapshotRepoUrlName() {
        return MAVEN_SNAPSHOT_REPO_URL.asCliOption();
    }

    @Override
    protected String getCurrentArtifactVersionName() {
        return CURRENT_ARTIFACT_VERSION.asCliOption();
    }

    @Override
    protected String getGroupIdName() {
        return GROUP_ID.asCliOption();
    }

    @Override
    protected String getArtifactIdName() {
        return ARTIFACT_ID.asCliOption();
    }

    @Override
    protected String getOldApiPathName() {
        return OLD_API_PATH.asCliOption();
    }

    @Override
    protected String getNewApiPathName() {
        return NEW_API_PATH.asCliOption();
    }
}
