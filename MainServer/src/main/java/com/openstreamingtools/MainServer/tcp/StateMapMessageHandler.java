package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.dj.stagelinq.PlayerState;
import com.openstreamingtools.MainServer.dj.stagelinq.State;
import com.openstreamingtools.MainServer.events.NewStageLinQDeviceEvent;
import com.openstreamingtools.MainServer.events.SendSubscribeMessageEvent;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StateMapSubscribeMessage;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
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

@Component
@MessageEndpoint
public class StateMapMessageHandler {
    @Autowired
    private ApplicationEventPublisher publisher;

    private static final Logger logger = LoggerFactory.getLogger(StateMapMessageHandler.class);

    @Async
    @EventListener(classes = NewStageLinQDeviceEvent.class)
    public void handleNewConnection(NewStageLinQDeviceEvent event){
       logger.debug("New device detected {}", event.getUnit());
       if (DirectoryService.getUnitByIP(event.getUnit().getIpString())!=null ){
           logger.debug("Subscribing to statemap");
            for(State state: PlayerState.values()){
                publisher.publishEvent(new SendSubscribeMessageEvent(this, new StateMapSubscribeMessage(state)));
            }
        }

    }

    @EventListener(classes = SendSubscribeMessageEvent.class)
    @ServiceActivator(outputChannel = "fromStateMap")
    public byte[] sendSubscribeMessage(SendSubscribeMessageEvent event){
        logger.debug("Subscribing to state {}", event.getSubscribeMessage().toString());
        return event.getSubscribeMessage().getBytes();
    }

    public void handleStateMapMessage(){}
}
