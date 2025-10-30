package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
class NewApiPathHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setNewApiPath(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.NEW_API_PATH;
    }

    @Override
    public String getHelpMessage() {
        return "The absolute path of the new api file";
    }
}