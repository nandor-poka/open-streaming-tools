package com.openstreamingtools.backend.messages.frontend;

import com.openstreamingtools.backend.dj.GenericUnit;
import com.openstreamingtools.backend.messages.MessageToFrontend;
import com.openstreamingtools.backend.messages.MessageType;

/**
 * Data transfer object for DJ unit data sent to the frontend.
 * Extends MessageToFrontend to include information about a DJ equipment unit.
 */
public class UnitData extends MessageToFrontend{
    /** The DJ unit object containing state and configuration */
    private GenericUnit unit;

    /**
     * Creates a UnitData message with unit information.
     *
     * @param type the message type
     * @param message the message description
     * @param unit the GenericUnit object to send
     */
    public UnitData(MessageType type, String message, GenericUnit unit) {
        super(type, message);
        this.unit = unit;
    }

    /**
     * Gets the unit data.
     *
     * @return the GenericUnit object
     */
    public GenericUnit getUnit() {
        return unit;
    }
}
