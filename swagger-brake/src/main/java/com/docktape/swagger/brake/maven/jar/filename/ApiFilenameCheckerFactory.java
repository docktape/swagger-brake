package com.docktape.swagger.brake.maven.jar.filename;

import java.util.Collection;
import java.util.TreeSet;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiFilenameCheckerFactory {
    /**
     * Creates an {@link ApiFileNameChecker} instance based on the provided configuredApiFilename argument.
     * @param configuredApiFilename the filename that is used to construct a proper instance f {@link ApiFileNameChecker}
     * @return the {@link ApiFileNameChecker}
     */
    public ApiFileNameChecker create(String configuredApiFilename) {
        Collection<ApiFileNameChecker> checkers = new TreeSet<>(OrderComparator.INSTANCE);
        checkers.add(new SwaggerApiFileNameChecker());
        if (StringUtils.isNotBlank(configuredApiFilename)) {
            checkers.add(new ConfigurableApiFileNameChecker(configuredApiFilename));
        }
        return new ApiFileNameCheckerChain(checkers);
    }
}
