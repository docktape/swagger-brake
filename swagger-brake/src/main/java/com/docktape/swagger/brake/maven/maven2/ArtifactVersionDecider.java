package com.docktape.swagger.brake.maven.maven2;

public abstract class ArtifactVersionDecider {
    public static boolean isSnapshot(String version) {
        return version.endsWith("-SNAPSHOT");
    }
}
