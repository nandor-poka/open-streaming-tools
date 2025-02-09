package com.openstreamingtools.MainServer.messages.stagelinqmessages;

public abstract class Service {

    public ServiceType type;
    public int unitPort;
    public ServiceType getType() {
        return type;
    }

    public int getUnitPort() {
        return unitPort;
    }
}
