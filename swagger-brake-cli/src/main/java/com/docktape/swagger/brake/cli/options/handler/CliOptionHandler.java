package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;

public interface CliOptionHandler {
    void handle(String optionValue, Options options);

    CliOption getHandledCliOption();

    String getHelpMessage();
}
