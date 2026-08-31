package com.openstreamingtools.backend.messages;

/**
 * Data transfer object for messages received from the frontend via WebSocket.
 * Contains a message type identifier and payload that can be JSON or plain text.
 */
public class MessageFromFrontend {
    /** Type of message being sent */
    protected MessageType type = MessageType.GENERAL_MESSAGE;
    /** Message payload (typically JSON-formatted string) */
    public String message;

    /**
     * Default constructor for JSON deserialization.
     */
    public MessageFromFrontend(){}

    /**
     * Creates a frontend message with just a message payload.
     *
     * @param message the message content
     */
    public MessageFromFrontend(String message) {
        this.message = message;
    }

    /**
     * Creates a frontend message with type and payload.
     *
     * @param type the message type
     * @param message the message content
     */
    public MessageFromFrontend(MessageType type, String message) {
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
