package com.openstreamingtools.backend.messaging;

import com.openstreamingtools.backend.config.OSTConfiguration;
import com.openstreamingtools.backend.services.stagelinq.DirectoryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class FrontendStartupHandler {
    @MessageMapping("/startup")
    @SendTo(OSTConfiguration.WEBSOCKET_DATA_PATH)
    public void frontendStartup(Message<String> message){
        log.debug("Frontend startup message received: {}",message.getPayload());
        DirectoryService.clearUnits();
        OSTConfiguration.setFrontEndRunning(true);
    }
}
