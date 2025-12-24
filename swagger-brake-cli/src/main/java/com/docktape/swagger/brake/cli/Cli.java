package com.docktape.swagger.brake.cli;

import java.util.Collection;

import com.docktape.swagger.brake.cli.options.CliHelpException;
import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.cli.options.CliOptionsProvider;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.Starter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Cli {
    private final CliOptionsProvider optionsProvider;

    /**
     * Starts Swagger Brake and returns the exit code.
     * <br>
     * 0 exit code means no breaking changes were detected and the check was executed successfully.<br>
     * 1 exit code means that breaking changes were found.<br>
     * 2 exit code means that there was an error during the execution.<br>
     * 3 exit code means that help was invoked.<br>
     * @return the exit code
     */
    public int start() {
        try {
            log.debug("Starting Swagger Brake CLI");
            Options options = optionsProvider.provide();
            Collection<BreakingChange> breakingChanges = Starter.start(options);
            if (CollectionUtils.isNotEmpty(breakingChanges)) {
                log.debug("List of Breaking changes found: {}", breakingChanges);
                return 1;
            } else {
                log.debug("No breaking changes found....");
                return 0;
            }
        } catch (CliHelpException e) {
            log.info(e.getMessage());
            return 3;
        } catch (Exception e) {
            log.error("Exception occurred", e);
            log.error("For help please use {}", CliOption.HELP.asCliOption());
            return 2;
        }
    }
}
