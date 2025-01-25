package com.openstreamingtools.MainServer.messages;

public class GeneralMessage {
    private String type = "General Message";
    public String message;

    public GeneralMessage(String message) {
        this.message = message;
    }

    public GeneralMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
