package com.openstreamingtools.MainServer.messages;

public class MessageFromFrontend {
    protected MessageType type = MessageType.GENERAL_MESSAGE;
    public String message; // should be a JSON string

    public MessageFromFrontend(){}

    public MessageFromFrontend(String message) {
        this.message = message;
    }

    public MessageFromFrontend(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {return message;}
}
