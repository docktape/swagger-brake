package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CurrentArtifactVersionHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setCurrentArtifactVersion(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.CURRENT_ARTIFACT_VERSION;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the current artifact version against the latest artifact resolution. Example: 1.0.0, 1.0.0-SNAPSHOT";
    }
}
