package com.docktape.swagger.brake.core;

import java.util.Collections;
import java.util.Set;

import lombok.Data;

@Data
public class CheckerOptions {
    private boolean deprecatedApiDeletionAllowed = true;
    private String betaApiExtensionName = "x-beta-api";
    private Set<String> excludedPaths = Collections.emptySet();
    private boolean strictValidation = true;
    private int maxLogSerializationDepth = 3;
    private int maxSchemaTransformationDepth = 50;

    public void setMaxLogSerializationDepth(int maxLogSerializationDepth) {
        if (maxLogSerializationDepth < 1 || maxLogSerializationDepth > 20) {
            throw new IllegalArgumentException("maxLogSerializationDepth must be between 1 and 20, got: " + maxLogSerializationDepth);
        }
        this.maxLogSerializationDepth = maxLogSerializationDepth;
    }

    public void setMaxSchemaTransformationDepth(int maxSchemaTransformationDepth) {
        if (maxSchemaTransformationDepth < 1 || maxSchemaTransformationDepth > 100) {
            throw new IllegalArgumentException("maxSchemaTransformationDepth must be between 1 and 100, got: " + maxSchemaTransformationDepth);
        }
        this.maxSchemaTransformationDepth = maxSchemaTransformationDepth;
    }
}
