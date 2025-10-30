package com.docktape.swagger.brake.cli.options.handler;

import static java.util.stream.Collectors.toSet;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IgnoredBreakingChangeRulesOptionHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            log.debug("Handling {} parameter with value {}", getHandledCliOption(), optionValue);
            String[] ignoredBreakingChangeRules = optionValue.split(",");
            log.debug("Splitted values are {}", ignoredBreakingChangeRules);
            options.setIgnoredBreakingChangeRules(Arrays.stream(ignoredBreakingChangeRules).map(String::trim).collect(toSet()));
        }

    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.IGNORED_BREAKING_CHANGE_RULES;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies which breaking changes shall be ignored. Rules have to be provided (find them in the doc). "
                + "Multiple values can be provided by using comma. Example: R001,R002";
    }
}
