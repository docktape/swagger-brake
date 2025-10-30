package com.docktape.swagger.brake.cli.options.handler;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.springframework.stereotype.Component;

@Component
public class MavenRepoUsernameHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (isNotBlank(optionValue)) {
            options.setMavenRepoUsername(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.MAVEN_REPO_USERNAME;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the username to access the Maven repository";
    }
}
