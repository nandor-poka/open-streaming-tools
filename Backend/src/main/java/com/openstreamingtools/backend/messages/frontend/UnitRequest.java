package com.openstreamingtools.backend.messages.frontend;

/**
 * Data transfer object for unit data requests from the frontend.
 * Contains the UUID of a DJ unit for which data is being requested.
 */
public class UnitRequest {
    /** The UUID of the unit being requested */
    private String uuid;

    /**
     * Creates a unit request for a specific UUID.
     *
     * @param uuid the unit UUID to request
     */
    public UnitRequest(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Default constructor for JSON deserialization.
     */
    public UnitRequest() {
    }

    /**
     * Gets the requested unit UUID.
     *
     * @return the UUID string
     */
    public String getUuid() {
        return uuid;
    }
}
