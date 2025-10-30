package com.docktape.swagger.brake.maven.jar.filename;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;

class ConfigurableApiFileNameChecker implements ApiFileNameChecker {
    private final String apiFilename;

    public ConfigurableApiFileNameChecker(String apiFilename) {
        if (StringUtils.isBlank(apiFilename)) {
            throw new IllegalArgumentException("apiFilename must not be empty");
        }
        this.apiFilename = apiFilename;
    }

    @Override
    public boolean isApiFile(String fileName) {
        return fileName.endsWith(apiFilename)
            || fileName.endsWith(apiFilename + ".yaml")
            || fileName.endsWith(apiFilename + ".yml")
            || fileName.endsWith(apiFilename + ".json");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
