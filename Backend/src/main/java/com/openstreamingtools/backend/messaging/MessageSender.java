package com.openstreamingtools.backend.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.backend.config.OSTConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.net.UnknownHostException;

@Component
@Slf4j
public class MessageSender {


    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static MessageSender instance;

    @Autowired
    public MessageSender(SimpMessagingTemplate template) throws SocketException, UnknownHostException {
        this.template = template;
        instance = this;
    }

    public static void sendMessage (String message){
        log.debug("sending to frontend: {}", message);
        instance.template.convertAndSend(OSTConfiguration.WEBSOCKET_DATA_PATH, message);
    }

    public static void sendMessage (Object message) throws JsonProcessingException {
        log.debug("sending to frontend: {}",instance.objectMapper.writeValueAsString(message));
        instance.template.convertAndSend(OSTConfiguration.WEBSOCKET_DATA_PATH, instance.objectMapper.writeValueAsString(message));
    }


}
