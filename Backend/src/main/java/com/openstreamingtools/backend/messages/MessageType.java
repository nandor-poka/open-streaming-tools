package com.openstreamingtools.backend.messages;

/**
 * Enumeration of message types for WebSocket communication between frontend and backend.
 * Defines standardized message type identifiers used for routing and handling different
 * kinds of data exchanges.
 */
public enum MessageType {
    /** Generic message type for text communications */
    GENERAL_MESSAGE("General Message"),
    /** Song data update including track and artist information */
    SONG_DATA("SongData"),
    /** Audio channel volume levels */
    CHANNEL_VOLUME_DATA("ChannelVolumeData"),
    /** StageLinQ discovery protocol messages */
    STAGELINQ_DISCOVERY_MESSAGE("StageLinQ Discovery Message"),
    /** DJ unit state and status data */
    UNIT_DATA("Unit data"),
    /** Acknowledgement of received messages */
    ACKNOWLEDMENT_MESSAGE("Acknowledgement message");

    private final String name;

    /**
     * Creates a message type with the given display name.
     *
     * @param type the display name for this message type
     */
    MessageType(String type) {
        this.name = type;
    }

    /**
     * Gets the display name of this message type.
     *
     * @return the message type name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the message type enum constant by its display name.
     *
     * @param name the display name to search for
     * @return the MessageType with the given name, or null if not found
     */
    public static MessageType getByName(String name) {
        for (MessageType type : MessageType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
