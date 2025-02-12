package com.openstreamingtools.MainServer.messaging;

import com.openstreamingtools.MainServer.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class FrontendStartupHandler {

    private static final Logger logger = LoggerFactory.getLogger(FrontendStartupHandler.class);


    @MessageMapping("/startup")
    @SendTo(Configuration.WEBSOCKET_DATA_PATH)
    public void frontendStartup(Message<String> message){
        logger.debug("Frontend startup message received: {}",message.getPayload());
        Configuration.setFrontEndRunning(true);
    }
}
