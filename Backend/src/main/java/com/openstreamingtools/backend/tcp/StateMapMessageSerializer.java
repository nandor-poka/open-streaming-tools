package com.openstreamingtools.backend.tcp;

import com.openstreamingtools.backend.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.backend.messages.stagelinqmessages.StateData;
import com.openstreamingtools.backend.messages.stagelinqmessages.StateMapMessage;
import com.openstreamingtools.backend.services.stagelinq.DirectoryService;
import com.openstreamingtools.backend.utils.Utils;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.Vector;

@Slf4j
public class StateMapMessageSerializer  implements Serializer<byte[]>, Deserializer<StateMapMessage<?>>  {

    private InputStream inputStream;

    @Override
    @Nonnull
    public StateMapMessage<?> deserialize(@Nonnull InputStream inputStream) {
        this.inputStream = inputStream;
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);

            /**
             * the header decides mostly what we are dealing with.
             * a full zero header means we are dealing with a service announcement
             * (this happens typically only once at the initial connection setup)
             * then it must be followed by an uuid
             * then the length of the service name
             * then the service name
             *  then the port
             *  A non-zero header means we are dealing with a state map message
             *  and the header is the length of the string that follows.
             *  a statemap starts with the string smaa magic string, then the state
             *  then length - smaa- state repeats
             */
            byte[] header = new byte[4];
            bis.read(header);
            if (Utils.convertBytesToInt(header) == DirectoryService.SERVICE_ANNOUNCEMENT){
                byte[] uuid = new byte[16];
                bis.read(uuid);
                if (Utils.convertBytesToInt(uuid) == 0){
                    return new StateMapMessage<>();
                }
                UUID deviceId = Utils.convertBytesToUUID(uuid);

                if (DirectoryService.hasUnit(deviceId)){
                    byte[] servceNameLength = new byte[4];
                    bis.read(servceNameLength);
                    int serviceNameLength = Utils.convertBytesToInt(servceNameLength);
                    byte[] serviceName = new byte[serviceNameLength];
                    bis.read(serviceName);
                    byte[] servicePort = new byte[2];
                    bis.read(servicePort);
                    int port = Utils.convertBytesToShort(servicePort);
                    ServiceAnnouncement sa = new ServiceAnnouncement(
                            DirectoryService.SERVICE_ANNOUNCEMENT,
                            deviceId, new String(serviceName, StandardCharsets.UTF_16BE), port);
                    StateMapMessage<ServiceAnnouncement> stateMapMessage = new StateMapMessage<>();
                    stateMapMessage.setData(sa);
                    log.info("Deserialized service announcement: {}", stateMapMessage);
                    return stateMapMessage;
                }
            }

            log.debug("Deserializing state map message with header: {}", Utils.convertBytesToInt(header));
            Vector<StateData> states = new Vector<>();
            int stateMapMessagelength = Utils.convertBytesToInt(header);
            byte[] messageBytes = bis.readNBytes(stateMapMessagelength);
            states.add(StateData.parseStateData(messageBytes));
            boolean endOfMessages = false;

            while (!endOfMessages){
                if (bis.read(header) < 4){
                    break;
                }
                stateMapMessagelength = Utils.convertBytesToInt(header);
                if(stateMapMessagelength > 0){
                    messageBytes = bis.readNBytes(stateMapMessagelength);
                    states.add(StateData.parseStateData(messageBytes));
                }else{
                    endOfMessages = true;
                }
            }

            StateMapMessage<StateData[]> stateMapMessage = new StateMapMessage<>();
            stateMapMessage.setData(states.toArray(new StateData[0]));
            log.warn("Deserialized message: {}", stateMapMessage);
            return stateMapMessage;
        } catch (Exception e) {
            log.error(e.toString());
        }
        log.warn("Could not deserialize message. Returning empty state map message.");
        return new StateMapMessage<>();
    }

    public void serialize(@NonNull byte[] message, OutputStream outputStream) throws IOException {
        outputStream.write(message);
        outputStream.flush();
    }
}
