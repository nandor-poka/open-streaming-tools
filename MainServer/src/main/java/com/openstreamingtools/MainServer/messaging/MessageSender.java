package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;

@Component
public class MessageSender {
    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Timer timer = new Timer();

    @Autowired
    public static MessageSender instance;

    @Autowired
    public MessageSender(SimpMessagingTemplate template) throws SocketException, UnknownHostException {
        this.template = template;
        instance = this;
        timer.scheduleAtFixedRate(new TestMessageSender(), 2000, 50);
    }

    public static void sendMessage (String message){
        instance.template.convertAndSend("/topic", message);
    }

    public static void sendMessage (Object message) throws JsonProcessingException {
        instance.template.convertAndSend("/topic", instance.objectMapper.writeValueAsString(message));
    }


}
