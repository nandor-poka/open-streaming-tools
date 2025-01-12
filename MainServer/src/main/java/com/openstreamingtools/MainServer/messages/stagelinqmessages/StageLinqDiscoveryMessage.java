package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.openstreamingtools.MainServer.dj.ModelType;
import com.openstreamingtools.MainServer.dj.StageLinQAction;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

public class StageLinqDiscoveryMessage {
    private StageLinQAction action;
    private ModelType modelType;
    private String softwareVersion;

    public StageLinqDiscoveryMessage(StageLinQAction action, ModelType modelType, String softwareVersion) {
        this.action = action;
        this.modelType = modelType;
        this.softwareVersion = softwareVersion;
    }

    @Override
    public String toString() {
        return "StageLinqDiscoveryMessage{" +
                "action=" + action +
                ", modelType=" + modelType +
                ", softwareVersion='" + softwareVersion + '\'' +
                '}';
    }


    public StageLinQAction getAction() {
        return action;
    }

    public void setAction(StageLinQAction action) {
        this.action = action;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }
}