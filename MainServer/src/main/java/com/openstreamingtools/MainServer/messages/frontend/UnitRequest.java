package com.openstreamingtools.MainServer.messages.frontend;

import org.springframework.boot.jackson.JsonComponent;

import java.util.UUID;

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
