package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.config.OSTConfiguration;

public enum ServiceType {
    STATEMAP("StateMap", OSTConfiguration.STATEMAP_SERVICE_PORT);

    private final String name;
    private final int port;

    ServiceType(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public static ServiceType getByName(String name) {
        for (ServiceType type : ServiceType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
