package com.openstreamingtools.MainServer.dj.stagelinq;

import com.openstreamingtools.MainServer.dj.GenericUnit;

import java.util.UUID;

public class DenonUnit extends GenericUnit {

    private UUID deviceID;
    private String ipString;

    public DenonUnit(UnitType type, ModelType modelType, String longName, int deckCount ) {
        super();
        this.type = type;
        this.modelType = modelType;
        this.LongName = longName;
        this.deckCount = deckCount;

    }

    public UUID getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(UUID deviceID) {
        this.deviceID = deviceID;
    }

    public String getIpString() {
        return ipString;
    }

    public void setIpString(String ipString) {
        this.ipString = ipString;
    }

    @Override
    public String toString() {
        return "DenonUnit{" +
                "deviceID=" + deviceID +
                ", ipString='" + ipString + '\'' +
                ", type=" + type +
                ", modelType=" + modelType +
                ", LongName='" + LongName + '\'' +
                ", version='" + version + '\'' +
                ", deckCount=" + deckCount +
                '}';
    }
}
