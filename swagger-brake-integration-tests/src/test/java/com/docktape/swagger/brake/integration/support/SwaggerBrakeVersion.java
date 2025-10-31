package com.docktape.swagger.brake.integration.support;

import lombok.Getter;

@Getter
public class SwaggerBrakeVersion {
    public static final SwaggerBrakeVersion V_LATEST_VERSION = new SwaggerBrakeVersion("2.6.0");

    private final String version;

    private SwaggerBrakeVersion(String version) {
        this.version = version;
    }
}
