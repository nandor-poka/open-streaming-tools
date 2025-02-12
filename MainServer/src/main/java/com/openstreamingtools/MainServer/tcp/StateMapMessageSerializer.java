package com.openstreamingtools.MainServer.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.dj.stagelinq.MixerState;
import com.openstreamingtools.MainServer.dj.stagelinq.PlayerState;
import com.openstreamingtools.MainServer.dj.stagelinq.State;
import com.openstreamingtools.MainServer.messages.frontend.ChannelVolumeData;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateData;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapMessage;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapMessage;
import com.openstreamingtools.MainServer.messaging.MessageSender;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

import static com.openstreamingtools.MainServer.utils.Utils.putIntegerToByteArray;

public class StateMapMessageSerializer  implements Deserializer<byte[]>, Serializer<byte[]> {


    private static final Logger logger = LoggerFactory.getLogger(StateMapMessageSerializer.class);


    @Override
    public byte[] deserialize(InputStream inputStream) throws IOException {
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
        // inputStream.mark();
        byte[] header = new byte[4];
        bis.read(header);
        if (Utils.convertBytesToInt(header) == DirectoryService.SERVICE_ANNOUNCEMENT){
            byte[] uuid = new byte[16];
            bis.read(uuid);
            if (Utils.convertBytesToInt(uuid) == 0){
                return new byte[0];
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
                return sa.toBytes();
            }
        }
        //todo for proper statemap messages that are expected after the initial connection
        // can be respones reject or actual state
        Vector<Byte> buffer = new Vector<>();
        int stateMapMessagelength = Utils.convertBytesToInt(header);
        byte [] messageBytes = bis.readNBytes(stateMapMessagelength);
        for (byte  b: checkStateData(messageBytes)){
            buffer.add(b);
        }
        // Check if there are other messages
        //
        boolean endOfMessages = false;
        while (!endOfMessages){
            stateMapMessagelength = Utils.convertBytesToInt(bis.readNBytes(4));
            if(stateMapMessagelength > 0){
                messageBytes = bis.readNBytes(stateMapMessagelength);
                for (byte  b: checkStateData(messageBytes)){
                    buffer.add(b);
                }
            }else{
                endOfMessages = true;
            }
        }
        byte[] response = new byte[buffer.size()];
        for (int i = 0 ; i< response.length;i++){
            response[i] = buffer.get(i);
        }
        return response;
    }

    private byte[] checkStateData(byte[] messageBytes) throws JsonProcessingException {
        if (new String(Arrays.copyOfRange(messageBytes, 0, 4),StandardCharsets.UTF_8)
                .equals(StateMapService.MAGIC_MARKER)){
            int dataType = Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 4,8));
            if (Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 4,8) )==StateMapService.MAGIC_MARKER_JSON){
                int nameLength = Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 8,12) );
                String stateDataName = new String(Arrays.copyOfRange(messageBytes, 12, 12+nameLength),StandardCharsets.UTF_16BE);
                String jsonString = new String(Arrays.copyOfRange(messageBytes, 12+nameLength+4, messageBytes.length),StandardCharsets.UTF_16BE);
                StateData stateData = StateData.parseStateData(messageBytes);
                State state = stateData.getState();
                if (state.equals(PlayerState.EngineDeck1TrackTrackName)
                        || state.equals(PlayerState.EngineDeck2TrackTrackName)
                        || state.equals(PlayerState.EngineDeck3TrackTrackName)
                        || state.equals(PlayerState.EngineDeck4TrackTrackName)) {
                    String[] songPathElements = stateData.getJsonString().split("\",\"")[0].split("/");
                    String fullSong =songPathElements[songPathElements.length-1];
                    String[] songElements = fullSong.split("\\.")[0].split("-");
                    //logger.debug("SongElements: {}",Arrays.toString(songElements));
                    if (songElements.length >=2){
                        MessageSender.sendMessage(new SongData(stateData.getDeckNum(), songElements[1], songElements[0]));
                    }

                }
                if (state.equals(PlayerState.EngineDeck1ExternalMixerVolume)
                        || state.equals(PlayerState.EngineDeck2ExternalMixerVolume)
                        || state.equals(PlayerState.EngineDeck3ExternalMixerVolume)
                        || state.equals(PlayerState.EngineDeck4ExternalMixerVolume)) {
                    //ExternalMixerVolume N{"type":0,"value":0.012926282361149788}
                    int volume = Math.round(Float.parseFloat(
                            stateData.getJsonString().split("\"value\":")[1].split("}")[0])*100);
                    logger.debug("volume {}",volume);
                    MessageSender.sendMessage(new ChannelVolumeData(stateData.getDeckNum(), volume));
                }
               // logger.debug("SateMap name {}, type {}, jsonString: {}", stateDataName,dataType, jsonString);
            }
            return messageBytes;
        }
        return new byte[0];
    }
    public void serialize(@NonNull byte[] message, OutputStream outputStream) throws IOException {
        outputStream.write(message);
        outputStream.flush();
    }
}
