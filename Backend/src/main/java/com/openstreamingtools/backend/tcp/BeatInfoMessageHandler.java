package com.openstreamingtools.backend.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.messages.stagelinqmessages.BeatInfoMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@MessageEndpoint
@Slf4j
public class BeatInfoMessageHandler {

    @ServiceActivator(inputChannel = "toBeatInfo")
    public byte[] handleBeatInfoMessage(BeatInfoMessage message) {
        log.debug("Received BeatInfoMessage: id={}, clock={}, deckCount={}",
                message.getId(), message.getClock(), message.getDeckCount());
        return new byte[0];
    }
}
