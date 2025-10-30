package com.docktape.swagger.brake.integration.docker;

import org.testcontainers.containers.Network;

public class NetworkHolder {
    private static final Network INSTANCE = Network.newNetwork();

    private NetworkHolder() {
    }

    public static Network getInstance() {
        return INSTANCE;
    }
}
