package com.docktape.swagger.brake.cli.options;

import java.util.List;

import com.docktape.swagger.brake.cli.options.handler.CliOptionHandler;
import com.docktape.swagger.brake.runner.Options;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CliOptionsProvider {
    private final List<CliOptionHandler> handlers;
    private final CliHelpProvider helpProvider;
    private final CliOptionsValidator cliOptionsValidator;
    private final Environment environment;

    /**
     * Constructs the {@link Options} instance based on the environment.
     * @return the {@link Options} instance.
     */
    public Options provide() {
        if (isHelpRequired()) {
            throw new CliHelpException(helpProvider.getHelp());
        }
        Options options = new Options();
        for (CliOptionHandler handler : handlers) {
            CliOption handledOption = handler.getHandledCliOption();
            String propertyValue = environment.getProperty(handledOption.asName());
            log.debug("Handling command line argument {} with value of {}", handledOption.asCliOption(), propertyValue);
            handler.handle(propertyValue, options);
        }
        cliOptionsValidator.validate(options);
        return options;
    }

    private boolean isHelpRequired() {
        return environment.getProperty(CliOption.HELP.asName()) != null;
    }
}
