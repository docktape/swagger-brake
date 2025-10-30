package com.docktape.swagger.brake.integration.project;

public class BuildFailureException extends RuntimeException {
    public BuildFailureException(String message) {
        super(message);
    }
}
