package com.docktape.swagger.brake.cli.options;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;

import java.util.Collection;

import com.docktape.swagger.brake.cli.options.handler.CliOptionHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CliHelpProvider {
    private final Collection<CliOptionHandler> optionHandlers;

    /**
     * Returns the help String for the CLI interface.
     * @return the help String.
     */
    public String getHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append(format("Usage: java -jar swagger-brake.jar %s=/home/user/something.yaml %s=/home/user/something_v2.yaml",
            CliOption.OLD_API_PATH.asCliOption(), CliOption.NEW_API_PATH.asCliOption()));
        sb.append(lineSeparator());
        sb.append(lineSeparator());
        sb.append("Parameters:");
        for (CliOptionHandler handler : optionHandlers) {
            sb.append(lineSeparator());
            sb.append("\t");
            sb.append(format("%s %s", formatPadCliOption(handler.getHandledCliOption()), handler.getHelpMessage()));
        }
        return sb.toString();
    }

    private String formatPadCliOption(CliOption option) {
        return StringUtils.rightPad(option.asCliOption(), 40);
    }

}
