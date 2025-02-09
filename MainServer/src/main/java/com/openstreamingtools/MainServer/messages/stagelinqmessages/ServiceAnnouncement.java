package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.config.Configuration;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

public class ServiceAnnouncement {

    private int messageId;
    private UUID deviceId;// the uudid of the device we are replying to, or the device that is sending us the message
    Vector<Service> services = new Vector<>();
    boolean isServer;

    public ServiceAnnouncement( int messageId, Service service){
        this.messageId = messageId;
        services.add(service);
        this.deviceId = Utils.convertBytesToUUID(Configuration.STAGELINQ_ACTING_AS.getValue());
        isServer = true;
    }

    public ServiceAnnouncement(int messageId, UUID deviceId, String serviceName, int port) {
        this.messageId = messageId;
        this.deviceId = deviceId;
        if (ServiceType.getByName(serviceName) != null) {
            switch (ServiceType.getByName(serviceName)) {
                case STATEMAP:
                    Service service = new StateMapService(ServiceType.STATEMAP, port);
                    services.add(service);
            }
        }
        isServer = false;
    }

    public boolean hasService(ServiceType type){
        for (Service service:services){
            if (service.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Service getService(ServiceType type){
        for (Service service:services){
            if (service.getType() == type){
                return service;
            }
        }
        return null;
    }


    public byte[] toBytes(){
        Vector<Byte> buffer = new Vector();
        // write service announcement tag
        for (byte b : Utils.convertIntegerToByteArray(messageId)){
            buffer.add(b);
        }
        // write UUID
        for (byte b : Utils.convertUUIDToBytes(deviceId)){
            buffer.add(b);
        }
        for (Service service:services){
            if (isServer){
                // write service announcement tag
                for (byte b : Utils.convertIntegerToByteArray(DirectoryService.SERVICE_ANNOUNCEMENT)){
                    buffer.add(b);
                }
                for (byte b: Configuration.STAGELINQ_ACTING_AS.getValue()){
                    buffer.add(b);
                }
            }

            for (byte b: Utils.getNetworkBytesWithLenghForString(service.getType().getName(), StandardCharsets.UTF_16BE)){
                buffer.add(b);
            }
            for (byte b : Utils.convertShortToByteArray(service.getType().getPort())){
                buffer.add(b);
            }
        }

        byte[] messageBytes = new byte[buffer.size()];
        for(int i = 0;i<messageBytes.length; i++){
            messageBytes[i]=buffer.get(i);
        }
        return messageBytes;
    }

    public static ServiceAnnouncement parseMessage(byte[] message){
        int messageId = Utils.convertBytesToInt(Arrays.copyOfRange(message, 0, 4));
        UUID deviceId = Utils.convertBytesToUUID(Arrays.copyOfRange(message, 4, 20));
        byte[] serviceNameLength = Arrays.copyOfRange(message, 20, 24);
        int serviceNameLengthInt = Utils.convertBytesToInt(serviceNameLength);
        byte[] serviceName = Arrays.copyOfRange(message, 24, 24+serviceNameLengthInt);
        byte[] servicePort = Arrays.copyOfRange(message, 24+serviceNameLengthInt, 26+serviceNameLengthInt);
        int port = Utils.convertBytesToShort(servicePort);
        switch (ServiceType.getByName(new String(serviceName, StandardCharsets.UTF_16BE))){
            case STATEMAP:
                return new ServiceAnnouncement(messageId,deviceId, new String(serviceName, StandardCharsets.UTF_16BE), port);
            default:
                return new ServiceAnnouncement(messageId, deviceId, "Unknown", 0);
        }
    }

    public UUID getDeviceId() {
        return deviceId;
    }
}
