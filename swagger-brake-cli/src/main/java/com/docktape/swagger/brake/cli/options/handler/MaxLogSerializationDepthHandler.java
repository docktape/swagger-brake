package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MaxLogSerializationDepthHandler implements CliOptionHandler {
    @Override
    public void handle(String optionValue, Options options) {
        if (StringUtils.isNotBlank(optionValue)) {
            try {
                int depth = Integer.parseInt(optionValue.trim());
                options.setMaxLogSerializationDepth(depth);
                log.debug("Set max log serialization depth to {}", depth);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "Invalid value for --max-log-serialization-depth: '" + optionValue + "'. Must be an integer between 1 and 20.", e);
            } catch (IllegalArgumentException e) {
                // Re-throw validation errors from CheckerOptions.setMaxLogSerializationDepth
                throw new IllegalArgumentException(
                    "Invalid value for --max-log-serialization-depth: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.MAX_LOG_SERIALIZATION_DEPTH;
    }

    @Override
    public String getHelpMessage() {
        return "Maximum depth for serializing Swagger/OpenAPI objects in logs to prevent StackOverflowError with circular references. Range: 1-20. Default: 3.";
    }
}
