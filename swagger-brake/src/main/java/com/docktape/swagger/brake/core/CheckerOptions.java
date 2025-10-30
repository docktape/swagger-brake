package com.docktape.swagger.brake.core;

import java.util.Collections;
import java.util.Set;

import lombok.Data;

@Data
public class CheckerOptions {
    private boolean deprecatedApiDeletionAllowed = true;
    private String betaApiExtensionName = "x-beta-api";
    private Set<String> excludedPaths = Collections.emptySet();
}
