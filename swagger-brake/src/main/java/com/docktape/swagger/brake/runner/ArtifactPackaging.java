package com.docktape.swagger.brake.runner;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum ArtifactPackaging {
    JAR("jar"), WAR("war");

    private final String packaging;

    public static ArtifactPackaging forPackaging(String packaging) {
        return Arrays.stream(ArtifactPackaging.values()).filter(p -> p.getPackaging().equalsIgnoreCase(packaging))
                .findAny().orElseThrow(() -> new IllegalArgumentException("Packaging " + packaging + " is not supported"));
    }
}
