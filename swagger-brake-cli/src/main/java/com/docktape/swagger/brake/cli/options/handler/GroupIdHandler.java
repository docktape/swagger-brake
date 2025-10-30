package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GroupIdHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            options.setGroupId(optionValue);
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.GROUP_ID;
    }

    @Override
    public String getHelpMessage() {
        return "Specifies the group id which will be used for latest version resolution";
    }
}
