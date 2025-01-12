package com.openstreamingtools.MainServer.udp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.dj.UnitUtils;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StageLinqDiscoveryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

import static com.openstreamingtools.MainServer.dj.UnitUtils.parseStageLinQDiscoveryMessage;

@Service
@Controller
public class StageLinQDiscoveryHandler {
    Logger logger = LoggerFactory.getLogger(StageLinQDiscoveryHandler.class);

    public static final String StageLinQChannelID = "StangelinQ Broadcast Discovery";
    private SimpMessagingTemplate template;
    private byte[] messageBytes;
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public StageLinQDiscoveryHandler(SimpMessagingTemplate template) {
        this.template = template;
    }

    @ServiceActivator(inputChannel =StageLinQChannelID)
    public void handleMessage(Message message) throws JsonProcessingException {
        messageBytes = (byte[]) message.getPayload();
        logger.debug(objectMapper.writeValueAsString(parseStageLinQDiscoveryMessage(messageBytes)));
        //if (new String(  Arrays.copyOfRange(messageBytes,0,3), StandardCharsets.UTF_8).equals(UnitUtils.STAGELINQ_MESSAGE_START)){

            this.template.convertAndSend("/topic", objectMapper.writeValueAsString( parseStageLinQDiscoveryMessage(messageBytes)));
        //}
        //慩牄鑖⒈䌲莼닥퇰scx4 "DISCOVERER_HOWDY_JP21
        //4.2.0蔽
        //616972449456e6fd2488433283bcf37bb2e5d1f0000000080073006300780034000000220044004900530043004f00560045005200450052005f0048004f005700440059005f00000008004a0050003200310000000a0034002e0032002e0030853d
        //logger.debug(msg);

          //  this.template.convertAndSend("/topic", msg);


    }

    @MessageMapping("/app/incoming")
    @SendTo("/topic")
    public String send(Message message){
       return "you sent us:"+message;
    }
}
