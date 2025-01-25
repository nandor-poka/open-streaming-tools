package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.messages.stagelinqmessages.DirectoryMessage;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@MessageEndpoint
public class DirectoryMessageHandler {

    Logger logger = LoggerFactory.getLogger(DirectoryMessageHandler.class);

    @ServiceActivator(inputChannel = "toDirectory")
    public byte[] handleTCPMessage(Message message) {
        DirectoryMessage directoryMessage = DirectoryMessage.parseMessage((byte[]) message.getPayload());
        //logger.debug("TCP Message Received:" + directoryMessage);
        if (DirectoryService.hasUnit(directoryMessage.getDeviceId())) {
            switch (directoryMessage.getMessageId()) {
                case DirectoryService.SERVICE_REQUEST:
                    if (DirectoryService.hasUnit(directoryMessage.getDeviceId())) {
                        return new ServiceAnnouncement().toBytes();

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
