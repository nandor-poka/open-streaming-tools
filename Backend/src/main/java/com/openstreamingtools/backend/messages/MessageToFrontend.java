package com.openstreamingtools.backend.messages;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract base class for messages sent to the frontend via WebSocket.
 * Provides common structure for all backend-to-frontend messages with type and payload.
 */
@Getter
@Setter
public abstract class MessageToFrontend {
    /** Type of message being sent */
    protected MessageType type = MessageType.GENERAL_MESSAGE;
    /** Message payload (typically JSON-formatted string) */
    public String message;

    /**
     * Default constructor for JSON deserialization.
     */
    public MessageToFrontend(){}

    /**
     * Creates a frontend message with just a message payload.
     *
     * @param message the message content
     */
    public MessageToFrontend(String message) {
        this.message = message;
    }

    /**
     * Creates a frontend message with type and payload.
     *
     * @param type the message type
     * @param message the message content
     */
    public MessageToFrontend(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Gets the message type.
     *
     * @return the type of this message
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Gets the message payload.
     *
     * @return the message content
     */
    public String getMessage() {return message;}

}
