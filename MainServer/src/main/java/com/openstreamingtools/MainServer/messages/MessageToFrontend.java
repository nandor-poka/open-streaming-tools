package com.openstreamingtools.MainServer.messages;

public abstract class MessageToFrontend {
    protected MessageType type = MessageType.GENERAL_MESSAGE;
    public String message; // should be a JSON string

    public MessageToFrontend(){}

    public MessageToFrontend(String message) {
        this.message = message;
    }

    public MessageToFrontend(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {return message;}

}
