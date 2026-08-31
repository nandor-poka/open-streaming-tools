package com.openstreamingtools.backend.messages.stagelinqmessages;

import com.openstreamingtools.backend.config.OSTConfiguration;

/**
 * Enumeration of StageLinQ service types with associated port numbers.
 * Represents different services available through StageLinQ protocol discovery.
 */
public enum ServiceType {
    /** StateMap service providing device state information */
    STATEMAP("StateMap", OSTConfiguration.STATEMAP_SERVICE_PORT),
    /** BeatInfo service providing beat/tempo information */
    BEATINFO("BeatInfo", OSTConfiguration.BEATINFO_SERVICE_PORT);

    private final String name;
    private final int port;

    /**
     * Creates a service type with name and port.
     *
     * @param name the service name
     * @param port the TCP port for this service
     */
    ServiceType(String name, int port) {
        this.name = name;
        this.port = port;
    }

    /**
     * Gets the service name.
     *
     * @return the service name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the TCP port for this service.
     *
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Retrieves the service type enum by name.
     *
     * @param name the service name to look up
     * @return the corresponding ServiceType, or null if not found
     */
    public static ServiceType getByName(String name) {
        for (ServiceType type : ServiceType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
