package com.openstreamingtools.MainServer.dj.stagelinq;

import com.openstreamingtools.MainServer.dj.GenericUnit;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.Service;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceType;

import java.util.UUID;
import java.util.Vector;

/**
 * A class representing a DENON DJ unit.
 */
public class DenonUnit extends GenericUnit {

    private UUID deviceID;
    private String ipString;
    private final Vector<Service> registeredServices = new Vector<Service>();
    public DenonUnit(UnitType type, ModelType modelType, String longName, int deckCount ) {
        super();
        this.type = type;
        this.modelType = modelType;
        this.longName = longName;
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
                ", LongName='" + longName + '\'' +
                ", version='" + version + '\'' +
                ", deckCount=" + deckCount +
                '}';
    }
    public void addService(Service service){
        registeredServices.add(service);
    }


    public boolean hasService(ServiceType serviceType){
        for (Service service : registeredServices){
            if (service.getType() == serviceType){
                return true;
            }

        }
        return false;
    }


}
