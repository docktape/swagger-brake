package com.docktape.swagger.brake.runner;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.docktape.swagger.brake.core.CheckerOptions;

@Component
public class CheckerOptionsFactory {
    /**
     * Converts the {@link Options} instance into a {@link CheckerOptions} instance.
     * @param options the {@link Options} to be converted.
     * @return the {@link CheckerOptions} instance.
     */
    public CheckerOptions create(Options options) {
        CheckerOptions checkerOptions = new CheckerOptions();
        checkerOptions.setDeprecatedApiDeletionAllowed(isDeprecatedApiDeletionAllowed(options));
        checkerOptions.setBetaApiExtensionName(getBetaApiExtensionName(checkerOptions.getBetaApiExtensionName(), options));
        checkerOptions.setExcludedPaths(options.getExcludedPaths());
        checkerOptions.setStrictValidation(isStrictValidationEnabled(options));
        checkerOptions.setMaxLogSerializationDepth(getMaxLogSerializationDepth(options, checkerOptions.getMaxLogSerializationDepth()));
        return checkerOptions;
    }

    private String getBetaApiExtensionName(String defaultBetaApiExtensionName, Options options) {
        return StringUtils.defaultString(options.getBetaApiExtensionName(), defaultBetaApiExtensionName);
    }

    private boolean isDeprecatedApiDeletionAllowed(Options options) {
        return BooleanUtils.toBooleanDefaultIfNull(options.getDeprecatedApiDeletionAllowed(), true);
    }

    private boolean isStrictValidationEnabled(Options options) {
        return BooleanUtils.toBooleanDefaultIfNull(options.getStrictValidation(), true);
    }

    private int getMaxLogSerializationDepth(Options options, int defaultValue) {
        Integer maxDepth = options.getMaxLogSerializationDepth();
        return maxDepth != null ? maxDepth : defaultValue;
    }
}
