package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.ArtifactPackaging;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ArtifactPackagingHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        ArtifactPackaging artifactPackaging = ArtifactPackaging.JAR;
        if (StringUtils.isNotBlank(optionValue)) {
            String cleanedOptionValue = optionValue.trim().toLowerCase();
            artifactPackaging = ArtifactPackaging.forPackaging(cleanedOptionValue);
        }
        options.setArtifactPackaging(artifactPackaging);
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.ARTIFACT_PACKAGING;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the artifact packaging. Could be jar or war. Used when resolving the latest artifact version. Defaults to jar if not specified.";
    }
}
