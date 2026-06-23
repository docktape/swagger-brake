package com.docktape.swagger.brake.core;

public interface BreakingChange {
    String getMessage();

    String getRuleCode();

    default Severity getSeverity() {
        return Severity.ERROR;
    }
}
