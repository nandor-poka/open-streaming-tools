package com.openstreamingtools.MainServer.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.dj.stagelinq.MixerState;
import com.openstreamingtools.MainServer.dj.stagelinq.PlayerState;
import com.openstreamingtools.MainServer.dj.stagelinq.State;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceType;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapSubscribeMessage;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Vector;

@Component
@MessageEndpoint
@Slf4j
public class StateMapMessageHandler {

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
                            //logger.debug("Subscribing to state {}", state);
                            for (byte b : new StateMapSubscribeMessage(state).getBytes()) {
                                buffer.add(b);
                            }

                        }
                        for (State state : MixerState.values()) {
                        //   logger.debug("Subscribing to state {}", state);
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
                        log.debug("Subscribed to services.");
                        return response;

                    }
                }
                return new byte[0];
            }
        }
        return new byte[0];
    }
}
