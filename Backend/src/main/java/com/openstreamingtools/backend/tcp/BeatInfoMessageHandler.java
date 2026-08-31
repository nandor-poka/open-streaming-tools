package com.openstreamingtools.backend.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.dj.stagelinq.MixerState;
import com.openstreamingtools.backend.dj.stagelinq.PlayerState;
import com.openstreamingtools.backend.dj.stagelinq.State;
import com.openstreamingtools.backend.messages.stagelinqmessages.ServiceAnnouncement;
import com.openstreamingtools.backend.messages.stagelinqmessages.ServiceType;
import com.openstreamingtools.backend.messages.stagelinqmessages.StateMapSubscribeMessage;
import com.openstreamingtools.backend.services.stagelinq.DirectoryService;
import com.openstreamingtools.backend.services.stagelinq.StateMapService;
import com.openstreamingtools.backend.utils.Utils;
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
public class BeatInfoMessageHandler {

    @ServiceActivator(inputChannel = "toBeatInfo")
    public byte[] handleBeatInfoMessage(Message message) throws JsonProcessingException {
        return new byte[0];
    }
}
