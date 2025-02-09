package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapMessage;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapMessage;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import static com.openstreamingtools.MainServer.utils.Utils.putIntegerToByteArray;

public class StateMapMessageSerializer  implements Deserializer<byte[]>, Serializer<byte[]> {


    private static final Logger logger = LoggerFactory.getLogger(StateMapMessageSerializer.class);


    @Override
    public byte[] deserialize(InputStream inputStream) throws IOException {

        if (inputStream.available() > 0) {
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
           // inputStream.mark();
            byte[] header = new byte[4];
            inputStream.read(header);
            if (Utils.convertBytesToInt(header) == DirectoryService.SERVICE_ANNOUNCEMENT){
                byte[] uuid = new byte[16];
                inputStream.read(uuid);
                if (Utils.convertBytesToInt(uuid) == 0){
                    return new byte[0];
                }
                UUID deviceId = Utils.convertBytesToUUID(uuid);

                if (DirectoryService.hasUnit(deviceId)){
                    byte[] servceNameLength = new byte[4];
                    inputStream.read(servceNameLength);
                    int serviceNameLength = Utils.convertBytesToInt(servceNameLength);
                    byte[] serviceName = new byte[serviceNameLength];
                    inputStream.read(serviceName);
                    byte[] servicePort = new byte[2];
                    inputStream.read(servicePort);
                    int port = Utils.convertBytesToShort(servicePort);
                    ServiceAnnouncement sa = new ServiceAnnouncement(
                            DirectoryService.SERVICE_ANNOUNCEMENT,
                            deviceId, new String(serviceName, StandardCharsets.UTF_16BE), port);
                    return sa.toBytes();
                }
            }
            //todo for proper statemap messages that are expected after the initial connection
            // can be respones reject or actual state
            int stateMapMessagelength = Utils.convertBytesToInt(header);
            byte [] messageBytes = inputStream.readNBytes(stateMapMessagelength);
            if (new String(Arrays.copyOfRange(messageBytes, 0, 4),StandardCharsets.UTF_8)
                    .equals(StateMapService.MAGIC_MARKER)){
                logger.debug(new String(Arrays.copyOfRange(messageBytes, 4, stateMapMessagelength),StandardCharsets.UTF_16BE));
            }
            // Check if there are other messages
            //
            boolean endOfMessages = false;
            while (!endOfMessages){
                stateMapMessagelength = Utils.convertBytesToInt(inputStream.readNBytes(4));
                if(stateMapMessagelength > 0){
                    messageBytes = inputStream.readNBytes(stateMapMessagelength);
                    if (new String(Arrays.copyOfRange(messageBytes, 0, 4),StandardCharsets.UTF_8)
                            .equals(StateMapService.MAGIC_MARKER)){
                        logger.debug(new String(Arrays.copyOfRange(messageBytes, 12, messageBytes.length),StandardCharsets.UTF_16BE) );
                        int dataType = Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 4,8));
                        if (Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 4,8) )==StateMapService.MAGIC_MARKER_JSON){
                            int nameLength = Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 8,12) );
                            String stateDateName = new String(Arrays.copyOfRange(messageBytes, 12, 12+nameLength),StandardCharsets.UTF_16BE);
                            String jsonString = new String(Arrays.copyOfRange(messageBytes, 12+nameLength+4, messageBytes.length),StandardCharsets.UTF_16BE);
                            logger.debug("SateMap name {}, type {}, jsonString: {}", stateDateName,dataType, jsonString);
                        }


                    }
                }else{
                    endOfMessages = true;
                }
            }

        }
        return new byte[0];
    }

    public void serialize(@NonNull byte[] message, OutputStream outputStream) throws IOException {
        //logger.info("Serializing {}", Arrays.toString(message));
        outputStream.write(message);
        outputStream.flush();
    }
}
