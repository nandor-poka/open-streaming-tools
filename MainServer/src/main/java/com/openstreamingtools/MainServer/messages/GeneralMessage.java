package com.openstreamingtools.MainServer.messages;

public class GeneralMessage {
    private String type = "General Message";
    public String message;

    public GeneralMessage(String message) {
        this.message = message;
    }
}
