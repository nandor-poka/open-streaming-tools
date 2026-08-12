package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.dj.stagelinq.ModelCode;
import com.openstreamingtools.MainServer.dj.stagelinq.ModelType;
import com.openstreamingtools.MainServer.dj.stagelinq.StageLinQAction;
import com.openstreamingtools.MainServer.messages.MessageToFrontend;
import com.openstreamingtools.MainServer.messages.MessageType;
import com.openstreamingtools.MainServer.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class StageLinQDiscoveryMessage extends MessageToFrontend {
    private StageLinQAction action;
    private ModelType modelType;
    private String softwareVersion;
    private UUID deviceID;
    private ModelCode modelCode;


    public StageLinQDiscoveryMessage(UUID deviceID, StageLinQAction action, ModelType modelType, ModelCode modelCode, String softwareVersion) {
        super(MessageType.STAGELINQ_DISCOVERY_MESSAGE,"StageLinQ device found.");
        this.action = action;
        this.modelType = modelType;
        this.softwareVersion = softwareVersion;
        this.deviceID = deviceID;
        this.modelCode = modelCode;
    }


    public static StageLinQDiscoveryMessage parse(byte[] rawMessage){
        //raw message as string airDV$C2{scx4"DISCOVERER_HOWDY_JP21
        //4.2.0=

        UUID deviceID = Utils.convertBytesToUUID(Arrays.copyOfRange(rawMessage, 4, 21));
        StageLinQAction action = StageLinQAction.getByValue(new String(Arrays.copyOfRange(rawMessage,37,71), StandardCharsets.UTF_16LE) );
        ModelType type = ModelType.getByValue(new String(Arrays.copyOfRange(rawMessage,25,33), StandardCharsets.UTF_16LE) );
        ModelCode name = ModelCode.getByValue(new String(Arrays.copyOfRange(rawMessage,75,83), StandardCharsets.UTF_16LE) );
        // must replace last byte with 0 because in the original message the last byte is padding only and is not UTF-16 encoded
        // for 5 UTF-16 chars we need an array of 10 bytes and the last needs to be fixed.
        byte[] versionRaw = Arrays.copyOfRange(rawMessage,87,97);
        versionRaw[9]=0;
        String version = new String(versionRaw, StandardCharsets.UTF_16LE);
        return new StageLinQDiscoveryMessage(deviceID,action, type, name, version);
    }

    @Override
    public String toString() {
        return "StageLinqDiscoveryMessage{" +
                "action=" + action +
                ", modelType=" + modelType +
                ", softwareVersion='" + softwareVersion  +
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

    public UUID getDeviceID() {
        return deviceID;
    }

    public ModelCode getModelCode() {
        return modelCode;
    }
}