package com.docktape.swagger.brake.runner;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * The class responsible for validating an {@link Options} instance.
 */
@Component
public class OptionsValidator {
    public final Map<String, Function<Options, String>> mavenConfigMap;

    /**
     * Constructs an {@link OptionsValidator} instance.
     */
    public OptionsValidator() {
        mavenConfigMap = ImmutableMap.of(
            getMavenRepoUrlName(), Options::getMavenRepoUrl,
            getMavenSnapshotRepoUrlName(), Options::getMavenSnapshotRepoUrl,
            getCurrentArtifactVersionName(), Options::getCurrentArtifactVersion,
            getGroupIdName(), Options::getGroupId,
            getArtifactIdName(), Options::getArtifactId
        );
    }

    /**
     * Executes the validation against an {@link Options} instance.
     * @param options the {@link Options} instance. Must be not null.
     * @throws IllegalArgumentException in case any kind of validation error is detected.
     */
    public void validate(Options options) {
        if (isBlank(options.getNewApiPath())) {
            throw new IllegalArgumentException(format("%s must be provided.", getNewApiPathName()));
        }
        if (isBlank(options.getOldApiPath()) && !isAnyMavenConfigurationSet(options)) {
            throw new IllegalArgumentException(format("Either %s must be provided or a Maven configuration.", getOldApiPathName()));
        }
        if (isBlank(options.getOldApiPath()) && isAnyMavenConfigurationSet(options)) {
            if (!isFullMavenConfigurationSet(options)) {
                Collection<String> missingMavenCliOptions = findMissingMavenConfiguration(options);
                if (isAnyRepoSet(options)) {
                    missingMavenCliOptions.remove(getMavenRepoUrlName());
                    missingMavenCliOptions.remove(getMavenSnapshotRepoUrlName());
                }
                if (CollectionUtils.isNotEmpty(missingMavenCliOptions)) {
                    String joinedMavenCliOptions = StringUtils.join(missingMavenCliOptions, ",");
                    throw new IllegalArgumentException(format("Partial Maven configuration detected, make sure you set %s as well.", joinedMavenCliOptions));
                }
            }
        }
    }

    private boolean isAnyRepoSet(Options options) {
        Function<Options, String> mavenRepoUrlFunction = mavenConfigMap.get(getMavenRepoUrlName());
        Function<Options, String> mavenSnapshotRepoUrlFunction = mavenConfigMap.get(getMavenSnapshotRepoUrlName());
        if (mavenRepoUrlFunction != null && mavenSnapshotRepoUrlFunction != null) {
            String mavenRepoUrl = mavenRepoUrlFunction.apply(options);
            String mavenSnapshotRepoUrl = mavenSnapshotRepoUrlFunction.apply(options);
            return isNotBlank(mavenRepoUrl) || isNotBlank(mavenSnapshotRepoUrl);
        }
        throw new IllegalStateException("mavenConfigMap is not configured properly");
    }

    private boolean isFullMavenConfigurationSet(Options options) {
        return findMissingMavenConfiguration(options).isEmpty();
    }

    private boolean isAnyMavenConfigurationSet(Options options) {
        return findMissingMavenConfiguration(options).size() < mavenConfigMap.size();
    }

    private Collection<String> findMissingMavenConfiguration(Options options) {
        return mavenConfigMap.entrySet().stream()
            .filter(e -> isBlank(e.getValue().apply(options)))
            .map(Map.Entry::getKey)
            .collect(toList());
    }

    protected String getMavenRepoUrlName() {
        return "mavenRepoUrl";
    }

    protected String getMavenSnapshotRepoUrlName() {
        return "mavenSnapshotRepoUrl";
    }

    protected String getCurrentArtifactVersionName() {
        return "currentArtifactVersion";
    }

    protected String getGroupIdName() {
        return "groupId";
    }

    protected String getArtifactIdName() {
        return "artifactId";
    }

    protected String getOldApiPathName() {
        return "oldApiPath";
    }

    protected String getNewApiPathName() {
        return "newApiPath";
    }
}
