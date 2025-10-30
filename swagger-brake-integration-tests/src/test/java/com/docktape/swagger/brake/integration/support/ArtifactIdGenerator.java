package com.docktape.swagger.brake.integration.support;

import org.apache.commons.lang3.RandomStringUtils;

public class ArtifactIdGenerator {
    public static String generate() {
        return RandomStringUtils.randomAlphanumeric(12);
    }
}
