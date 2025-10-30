package com.docktape.swagger.brake.integration.artifactory;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.Network;

@Slf4j
public class ArtifactoryServerPool {
    private static final ArtifactoryServerPool INSTANCE = new ArtifactoryServerPool();

    private final Object mutex = new Object();
    private ArtifactoryServer server = null;

    private ArtifactoryServerPool() {
    }

    public ArtifactoryServer provide(Network network) {
        synchronized (mutex) {
            if (server == null) {
                server = new ArtifactoryServer(network);
                server.initialize();
            }
            return server;
        }
    }

    public static ArtifactoryServerPool getInstance() {
        return INSTANCE;
    }
}
