package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.config.Configuration;
import com.openstreamingtools.MainServer.dj.GenericUnit;
import com.openstreamingtools.MainServer.messages.MessageType;
import com.openstreamingtools.MainServer.messages.frontend.UnitData;
import com.openstreamingtools.MainServer.messages.frontend.UnitRequest;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DurationFormat;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class UnitRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(UnitRequestHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/getUnit")
    @SendTo(Configuration.WEBSOCKET_DATA_PATH)
    public String frontendStartup(Message<String> message) throws JsonProcessingException {
        logger.debug("Frontend requested unit data for : {}",message.getPayload());
        UnitRequest unitRequest = objectMapper.readValue(message.getPayload(), UnitRequest.class);
        GenericUnit unit = DirectoryService.getUnit(UUID.fromString(unitRequest.getUuid()));
        UnitData unitData = new UnitData(MessageType.UNIT_DATA,"Denon unit",unit);
        DirectoryService.getUnit(UUID.fromString(unitRequest.getUuid())).acknowledged=true;
        return objectMapper.writeValueAsString(unitData);
    }
}
