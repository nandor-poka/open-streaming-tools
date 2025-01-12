package com.openstreamingtools.MainServer.udp;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
@Controller
public class StageLinQMessageHandler{

    public static final String StageLinQChannelID = "StangelinQ Broadcast Discovery";


    @ServiceActivator(inputChannel =StageLinQChannelID)
    @SendTo("/stagelinq")
    public void handleMessage(Message message)
    {
       String msg = new String((byte[]) message.getPayload());
       System.out.println(msg);
       //return msg;
    }

    @MessageMapping("/stagelinqdiscovery")
    public void send(Message message){
        System.out.println(message);
    }
}
