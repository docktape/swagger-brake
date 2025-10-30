package com.docktape.swagger.brake.cli.options.handler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class VerboseHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (optionValue == null) {
            return;
        }

        if (optionValue.isEmpty()) {
            org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger("com.docktape.swagger.brake");
            if (slf4jLogger instanceof Logger) {
                Logger logger = (Logger) slf4jLogger;
                logger.setLevel(Level.DEBUG);
                Appender<ILoggingEvent> verboseAppender = getVerboseAppender();
                if (verboseAppender != null) {
                    logger.detachAppender("INFO_STDOUT");
                    logger.addAppender(verboseAppender);
                }
            }
        }
    }

    private Appender<ILoggingEvent> getVerboseAppender() {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        return rootLogger.getAppender("VERBOSE_STDOUT");
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.VERBOSE;
    }

    @Override
    public String getHelpMessage() {
        return "Sets verbose logging";
    }
}
