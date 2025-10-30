package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class OutputPathHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setOutputFilePath(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.OUTPUT_PATH;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the folder for the output. Only applicable in case of file output formats.";
    }
}
