package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DeprecatedApiDeletionAllowedHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setDeprecatedApiDeletionAllowed(BooleanUtils.toBooleanObject(optionValue));
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.DEPRECATED_API_DELETION_ALLOWED;
    }

    @Override
    public String getHelpMessage() {
        return "Whether to allow the deletion of deprecated APIs. Defaults to true.";
    }
}
