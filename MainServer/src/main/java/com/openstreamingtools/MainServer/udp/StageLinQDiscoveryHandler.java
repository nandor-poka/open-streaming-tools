package com.openstreamingtools.MainServer.udp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.config.Configuration;
import com.openstreamingtools.MainServer.dj.UnitUtils;
import com.openstreamingtools.MainServer.dj.stagelinq.DenonUnit;
import com.openstreamingtools.MainServer.dj.stagelinq.ModelCode;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServerDiscoveryMessage;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StageLinQDiscoveryMessage;
import com.openstreamingtools.MainServer.messaging.MessageSender;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;

import static com.openstreamingtools.MainServer.config.Configuration.STAGELINQ_BORADCAST_IP;
import static com.openstreamingtools.MainServer.config.Configuration.STAGELINQ_BROADCAST_PORT;


@Service
@Controller
@Component
public class StageLinQDiscoveryHandler {
    Logger logger = LoggerFactory.getLogger(StageLinQDiscoveryHandler.class);

    public static final String StageLinQChannelID = "StangelinQ Broadcast Discovery";
    private final ObjectMapper objectMapper = new ObjectMapper();
    DatagramSocket broadcastSocket = new DatagramSocket(STAGELINQ_BROADCAST_PORT, InetAddress.getLocalHost());

    byte[] staticTestMessageBytes = new byte[]{
            (byte)0x61,
            (byte)0x69,(byte)0x72,(byte)0x44,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0x4a,(byte)0x1c,
            (byte)0x9b,(byte)0xba,(byte)0x88,(byte)0xb4,(byte)0xbe,(byte)0x19,(byte)0xa3,(byte)0xd1,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x00,(byte)0x53,
            (byte)0x00,(byte)0x4c,(byte)0x00,(byte)0x4a,(byte)0x00,(byte)0x53,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x22,(byte)0x00,(byte)0x44,(byte)0x00,(byte)0x49,(byte)0x00,(byte)0x53,(byte)0x00,(byte)0x43,(byte)0x00,
            (byte)0x4f,(byte)0x00,(byte)0x56,(byte)0x00,(byte)0x45,(byte)0x00,(byte)0x52,(byte)0x00,(byte)0x45,(byte)0x00,(byte)0x52,(byte)0x00,
            (byte)0x5f,(byte)0x00,(byte)0x48,(byte)0x00,(byte)0x4f,(byte)0x00,(byte)0x57,(byte)0x00,(byte)0x44,(byte)0x00,(byte)0x59,(byte)0x00,
            (byte)0x5f,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x16,(byte)0x00,(byte)0x73,(byte)0x00,(byte)0x74,(byte)0x00,(byte)0x61,(byte)0x00,
            (byte)0x67,(byte)0x00,(byte)0x65,(byte)0x00,(byte)0x6c,(byte)0x00,(byte)0x69,(byte)0x00,(byte)0x6e,(byte)0x00,(byte)0x71,(byte)0x00,
            (byte)0x6a,(byte)0x00,(byte)0x73,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x14,(byte)0x00,(byte)0x32,(byte)0x00,(byte)0x2e,
            (byte)0x00,(byte)0x30,(byte)0x00,(byte)0x2e,(byte)0x00,(byte)0x30,(byte)0x00,(byte)0x2d,(byte)0x00,(byte)0x42,(byte)0x00,
            (byte)0x65,(byte)0x00,(byte)0x74,(byte)0x00,(byte)0x61,(byte)0xea,(byte)0x60
    };

    public StageLinQDiscoveryHandler() throws SocketException, UnknownHostException {
    }

    @ServiceActivator(inputChannel = StageLinQChannelID)
    public void handleMessage(Message message) throws JsonProcessingException {

        if (!Configuration.isFrontEndRunning()){
            logger.debug("Waiting for frontend to connect.");
            return;
        }
        byte[] messageBytes = (byte[]) message.getPayload();
        try {
            if (message.getHeaders().get(IpHeaders.IP_ADDRESS).equals(InetAddress.getLocalHost().getHostAddress()) ){
                return;
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        StageLinQDiscoveryMessage disMessage = StageLinQDiscoveryMessage.parse(messageBytes);
        // Retrun if we detect message from unknown model must change later
        if (disMessage.getModelCode().equals(ModelCode.UNKOWN)){
            return;
        }
        DenonUnit unit = (DenonUnit) UnitUtils.unitMapping.get(disMessage.getModelCode());
        unit.setDeviceID(disMessage.getDeviceID());
        unit.setVersion(disMessage.getSoftwareVersion());
        unit.setIpString((String) message.getHeaders().get(IpHeaders.IP_ADDRESS));
        if(!DirectoryService.hasUnit(unit.getDeviceID())){
            DirectoryService.addUnit(unit);
            MessageSender.sendMessage(objectMapper.writeValueAsString(disMessage));
        }

        ServerDiscoveryMessage broadcastMessage = new ServerDiscoveryMessage();
        DatagramPacket packet = new DatagramPacket(broadcastMessage.getMessageAsBytes(), broadcastMessage.getMessageAsBytes().length);
        packet.setSocketAddress(new InetSocketAddress(STAGELINQ_BORADCAST_IP, STAGELINQ_BROADCAST_PORT));
        try {
            broadcastSocket.setBroadcast(true);
            broadcastSocket.send(packet);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
