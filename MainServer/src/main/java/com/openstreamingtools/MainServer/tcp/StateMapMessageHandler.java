package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.dj.UnitUtils;
import com.openstreamingtools.MainServer.dj.stagelinq.MixerState;
import com.openstreamingtools.MainServer.dj.stagelinq.PlayerState;
import com.openstreamingtools.MainServer.dj.stagelinq.State;
import com.openstreamingtools.MainServer.events.NewStageLinQDeviceEvent;
import com.openstreamingtools.MainServer.events.SendSubscribeMessageEvent;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.Service;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceType;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapMessage;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapSubscribeMessage;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Vector;

@Component
@MessageEndpoint
public class StateMapMessageHandler {


    private static final Logger logger = LoggerFactory.getLogger(StateMapMessageHandler.class);

/*    @Async
    @EventListener(classes = NewStageLinQDeviceEvent.class)
    public void handleNewConnection(NewStageLinQDeviceEvent event){
       logger.debug("New device detected {}", event.getUnit());
       if (DirectoryService.getUnitByIP(event.getUnit().getIpString())!=null ){
           logger.debug("Subscribing to statemap");
            for(State state: PlayerState.values()){
                publisher.publishEvent(new SendSubscribeMessageEvent(this, new StateMapSubscribeMessage(state)));
            }
        }

    }*/

/*    @EventListener(classes = SendSubscribeMessageEvent.class)
    @ServiceActivator(outputChannel = "fromStateMap")
    public byte[] sendSubscribeMessage(SendSubscribeMessageEvent event){
        logger.debug("Subscribing to state {}", event.getSubscribeMessage().toString());
        return event.getSubscribeMessage().getBytes();
    }*/
    @ServiceActivator(inputChannel = "toStateMap")
    public byte[] handleStateMapMessage(Message<byte[]> message) {
        byte[] msg = message.getPayload();
        if (msg.length == 0){
            return new byte[0];
        }
        // TODO use registered services to check if we should expect service announcement message or normal state map message
        // //TODO check service announcement from deck and then send subscribtions
        if (!DirectoryService.getUnitByIP((String) message.getHeaders().get(IpHeaders.IP_ADDRESS)).hasService(ServiceType.STATEMAP)) {
            {
                if (Utils.convertBytesToInt(Arrays.copyOfRange(msg, 0, 4)) == 0) {
                    ServiceAnnouncement sa = ServiceAnnouncement.parseMessage(msg);
                    if (sa.hasService(ServiceType.STATEMAP)) {
                        Vector<Byte> buffer = new Vector<Byte>();
                        for (State state : PlayerState.values()) {
                            logger.debug("Subscribing to state {}", state);
                            for (byte b : new StateMapSubscribeMessage(state).getBytes()) {
                                buffer.add(b);
                            }

                        }
                        for (State state : MixerState.values()) {
                            logger.debug("Subscribing to state {}", state);
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
            }

            return new byte[0];

        }
        return new byte[0];
    }


}
