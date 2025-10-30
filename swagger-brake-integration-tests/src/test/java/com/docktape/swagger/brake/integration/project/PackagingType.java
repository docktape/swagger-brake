package com.docktape.swagger.brake.integration.project;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PackagingType {
    JAR("jar"), WAR("war");

    private final String packaging;
}
