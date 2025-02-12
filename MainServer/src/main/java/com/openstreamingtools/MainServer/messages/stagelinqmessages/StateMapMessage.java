package com.openstreamingtools.MainServer.messages.stagelinqmessages;

public class StateMapMessage <T>{

    private StateData message;

    public StateMapMessage() {
    }

    public StateMapMessage(StateData message) {
        this.message = message;
    }

    public StateData getMessage() {
        return message;
    }
}
