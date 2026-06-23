package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ServerUrlChangeEnabledHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setServerUrlChangeEnabled(BooleanUtils.toBooleanObject(optionValue));
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.SERVER_URL_CHANGE_ENABLED;
    }

    @Override
    public String getHelpMessage() {
        return "Whether to enable detection of server URL changes. Defaults to false.";
    }
}
