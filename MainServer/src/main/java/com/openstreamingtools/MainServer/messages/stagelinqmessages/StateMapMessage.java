package com.openstreamingtools.MainServer.messages.stagelinqmessages;

public class StateMapMessage <T>{

    private T message;

    public StateMapMessage() {
    }

    public StateMapMessage(T message) {
        this.message = message;
    }

    public T getMessage() {
        return message;
    }
}
