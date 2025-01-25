package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.config.Configuration;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.Vector;

public class ServiceAnnouncement {

    private int messageId = DirectoryService.SERVICE_ANNOUNCEMENT;
   private UUID deviceId;// the uudid of the device we are replying to
    Service[] services = new Service[]{new StateMapService()};

    public ServiceAnnouncement() {
    }

    public ServiceAnnouncement(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public byte[] toBytes(){
        Vector<Byte> buffer = new Vector();
        // write service announcement tag
        for (byte b : Utils.convertIntegerToByteArray(DirectoryService.SERVICE_REQUEST)){
            buffer.add(b);
        }
        // write UUID
        for (byte b : Configuration.STAGELINQ_ACTING_AS.getValue()){
            buffer.add(b);
        }
        for (Service service:services){
            // write service announcement tag
            for (byte b : Utils.convertIntegerToByteArray(messageId)){
                buffer.add(b);
            }
            for (byte b: Configuration.STAGELINQ_ACTING_AS.getValue()){
                buffer.add(b);
            }

            for (byte b: Utils.getNetworkBytesWithLenghForString(service.name, StandardCharsets.UTF_16BE)){
                buffer.add(b);
            }
            for (byte b : Utils.convertShortToByteArray(service.port)){
                buffer.add(b);
            }
        }

        byte[] messageBytes = new byte[buffer.size()];
        for(int i = 0;i<messageBytes.length; i++){
            messageBytes[i]=buffer.get(i);
        }
        return messageBytes;
    }
}
