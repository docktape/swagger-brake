package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class BetaApiExtensionNameHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setBetaApiExtensionName(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.BETA_API_EXTENSION_NAME;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the beta API extension attribute name. Defaults to 'x-beta-api'.";
    }
}
