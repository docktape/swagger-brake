package com.docktape.swagger.brake.cli.options.handler;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExcludedPathsHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            log.debug("Handling {} parameter with value {}", getHandledCliOption(), optionValue);
            String[] excludedPaths = optionValue.split(",");
            log.debug("Splitted values are {}", excludedPaths);
            options.setExcludedPaths(Arrays.stream(excludedPaths).collect(toSet()));
        }

    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.EXCLUDED_PATHS;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies paths that should be excluded from the check. Multiple values can be provided by using comma.";
    }
}
