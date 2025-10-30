package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MavenSnapshotRepoUrlHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setMavenSnapshotRepoUrl(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.MAVEN_SNAPSHOT_REPO_URL;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the Maven snapshot repository URL where the latest artifact denoted by the groupId and artifactId will be downloaded";
    }
}
