package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.core.Severity;
import com.docktape.swagger.brake.runner.Options;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FailOnSeverityHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            log.debug("Handling {} parameter with value {}", getHandledCliOption(), optionValue);
            try {
                Severity severity = Severity.valueOf(optionValue.trim().toUpperCase());
                options.setFailOnSeverity(severity);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid severity value '{}', keeping default ERROR", optionValue);
            }
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.FAIL_ON_SEVERITY;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the minimum severity level at which breaking changes cause a CI failure. "
                + "Accepted values: error, warning, info. Defaults to error.";
    }
}
