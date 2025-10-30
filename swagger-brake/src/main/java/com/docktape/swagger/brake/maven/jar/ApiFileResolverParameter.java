package com.docktape.swagger.brake.maven.jar;

import java.io.File;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class ApiFileResolverParameter {
    private final File apiJar;
    private final String configuredApiFilename;
}
