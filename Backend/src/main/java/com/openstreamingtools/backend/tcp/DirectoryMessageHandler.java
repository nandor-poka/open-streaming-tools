package com.openstreamingtools.backend.tcp;

import com.openstreamingtools.backend.messages.stagelinqmessages.DirectoryMessage;
import com.openstreamingtools.backend.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.backend.services.stagelinq.DirectoryService;
import com.openstreamingtools.backend.services.stagelinq.StateMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@MessageEndpoint
@Slf4j
public class DirectoryMessageHandler {

    @ServiceActivator(inputChannel = "toDirectory")
    public byte[] handleTCPMessage(Message<DirectoryMessage> message) {
        DirectoryMessage directoryMessage = message.getPayload();
        log.debug("TCP Message Received:" + directoryMessage);
        if (DirectoryService.hasUnit(directoryMessage.getDeviceId())) {
            switch (directoryMessage.getMessageId()) {
                case DirectoryService.SERVICE_REQUEST:
                    if (DirectoryService.hasUnit(directoryMessage.getDeviceId())) {
                        return new ServiceAnnouncement(DirectoryService.SERVICE_REQUEST,
                                new StateMapService()).toBytes();
                    }
                    return new byte[0];
                case DirectoryService.SERVICE_ANNOUNCEMENT:
                    return new byte[0];
                case DirectoryService.TIMESTAMP:
                    // TODO send reply
                    return new byte[0];
                default:
                    return new byte[0];
            }
        }
        return new byte[0];
    }


}
