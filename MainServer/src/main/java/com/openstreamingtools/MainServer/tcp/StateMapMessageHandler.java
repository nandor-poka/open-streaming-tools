package com.openstreamingtools.MainServer.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.dj.stagelinq.MixerState;
import com.openstreamingtools.MainServer.dj.stagelinq.PlayerState;
import com.openstreamingtools.MainServer.dj.stagelinq.State;
import com.openstreamingtools.MainServer.messages.frontend.ChannelVolumeData;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceType;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateData;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapSubscribeMessage;
import com.openstreamingtools.MainServer.messaging.MessageSender;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Vector;

@Component
@MessageEndpoint
public class StateMapMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(StateMapMessageHandler.class);

    @ServiceActivator(inputChannel = "toStateMap")
    public byte[] handleStateMapMessage(Message message) throws JsonProcessingException {
        byte[] msg =(byte[]) message.getPayload();
        if (msg.length == 0) {
            return new byte[0];
        }
        // TODO use registered services to check if we should expect service announcement message or normal state map message
        // TODO check service announcement from deck and then send subscribtions
        if (!DirectoryService.getUnitByIP((String) message.getHeaders().get(IpHeaders.IP_ADDRESS)).hasService(ServiceType.STATEMAP)) {
            {
                if (Utils.convertBytesToInt(Arrays.copyOfRange(msg, 0, 4)) == 0) {
                    ServiceAnnouncement sa = ServiceAnnouncement.parseMessage(msg);
                    if (sa.hasService(ServiceType.STATEMAP)) {
                        Vector<Byte> buffer = new Vector<Byte>();
                        for (State state : PlayerState.values()) {
                          //  logger.debug("Subscribing to state {}", state);
                            for (byte b : new StateMapSubscribeMessage(state).getBytes()) {
                                buffer.add(b);
                            }

                        }
                        for (State state : MixerState.values()) {
                        //    logger.debug("Subscribing to state {}", state);
                            for (byte b : new StateMapSubscribeMessage(state).getBytes()) {
                                buffer.add(b);
                            }
                        }
                        byte[] response = new byte[buffer.size()];
                        for (int i = 0; i < response.length; i++) {
                            response[i] = buffer.get(i);
                        }
                        DirectoryService.getUnit(sa.getDeviceId()).addService(
                                new StateMapService(ServiceType.STATEMAP, sa.getService(ServiceType.STATEMAP).getUnitPort()));
                        return response;

                    }
                }
                // handle array of messages that
                int pos = 0;
                while (pos <= msg.length) {
                    int stateDataLength = Utils.convertBytesToInt(Arrays.copyOfRange(msg, pos, 4));
                    pos += 4;
                    byte[] messageBytes = Arrays.copyOfRange(msg, pos, stateDataLength);
                    pos += stateDataLength;
                    StateData stateData = StateData.parseStateData(messageBytes);
                    State state = stateData.getState();
                    if (state.equals(PlayerState.EngineDeck1TrackTrackName)
                            || state.equals(PlayerState.EngineDeck2TrackTrackName)
                            || state.equals(PlayerState.EngineDeck3TrackTrackName)
                            || state.equals(PlayerState.EngineDeck4TrackTrackName)) {
                        String[] songElements = stateData.getJsonString().split("\",\"")[0].split("\\.")[0].split("-");

                        MessageSender.sendMessage(new SongData(stateData.getDeckNum(), songElements[0], songElements[1])
                        );
                    }
                    if (state.equals(PlayerState.EngineDeck1ExternalMixerVolume)
                            || state.equals(PlayerState.EngineDeck2ExternalMixerVolume)
                            || state.equals(PlayerState.EngineDeck3ExternalMixerVolume)
                            || state.equals(PlayerState.EngineDeck4ExternalMixerVolume)) {
                        //ExternalMixerVolume N{"type":0,"value":0.012926282361149788}
                        int volume = (int)Math.round(Math.ceil(Float.parseFloat(
                                stateData.getJsonString().split("\"value\":")[1].split("}")[0])*100));
                        MessageSender.sendMessage(new ChannelVolumeData(stateData.getDeckNum(), volume));
                    }

                }

                return new byte[0];

            }
        }
        return new byte[0];
    }
}
