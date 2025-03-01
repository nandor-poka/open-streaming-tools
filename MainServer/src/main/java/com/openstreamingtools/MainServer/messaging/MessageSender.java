package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;

@Component
public class MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Timer timer = new Timer();


    public static MessageSender instance;

    @Autowired
    public MessageSender(SimpMessagingTemplate template) throws SocketException, UnknownHostException {
        this.template = template;
        instance = this;
     //   timer.scheduleAtFixedRate(new TestMessageSender(), 2000, 50);
    }

    public static void sendMessage (String message){
        instance.template.convertAndSend(Configuration.WEBSOCKET_DATA_PATH, message);
    }

    public static void sendMessage (Object message) throws JsonProcessingException {
        logger.debug("sending to frontend: {}",instance.objectMapper.writeValueAsString(message));
        instance.template.convertAndSend(Configuration.WEBSOCKET_DATA_PATH, instance.objectMapper.writeValueAsString(message));
    }


}
