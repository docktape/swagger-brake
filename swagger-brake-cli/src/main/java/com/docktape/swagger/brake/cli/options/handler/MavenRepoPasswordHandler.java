package com.docktape.swagger.brake.cli.options.handler;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.springframework.stereotype.Component;

@Component
public class MavenRepoPasswordHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (isNotBlank(optionValue)) {
            options.setMavenRepoPassword(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.MAVEN_REPO_PASSWORD;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the password to access the Maven repository";
    }
}
