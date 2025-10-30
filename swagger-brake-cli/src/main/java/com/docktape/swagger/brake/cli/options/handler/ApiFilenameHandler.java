package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ApiFilenameHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setApiFilename(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.API_FILENAME;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the API filename Swagger Brake is going to search for";
    }
}
