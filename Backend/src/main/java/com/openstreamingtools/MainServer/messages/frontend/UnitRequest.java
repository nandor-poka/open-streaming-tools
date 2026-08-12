package com.openstreamingtools.MainServer.messages.frontend;

public class UnitRequest {
   private String uuid;

    public UnitRequest(String uuid) {
        this.uuid = uuid;
    }

    public UnitRequest() {
    }

    public String getUuid() {
        return uuid;
    }
}
