package com.openstreamingtools.MainServer.udp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;

import static com.openstreamingtools.MainServer.config.OSTConfiguration.STAGELINQ_BORADCAST_IP;
import static com.openstreamingtools.MainServer.config.OSTConfiguration.STAGELINQ_BROADCAST_PORT;


@Service
@Controller
@Component
public class StageLinQDiscoveryHandler {
    Logger logger = LoggerFactory.getLogger(StageLinQDiscoveryHandler.class);

    public static final String StageLinQChannelID = "StangelinQ Broadcast Discovery";
    private final ObjectMapper objectMapper = new ObjectMapper();
    DatagramSocket broadcastSocket = new DatagramSocket(STAGELINQ_BROADCAST_PORT, InetAddress.getLocalHost());


    public StageLinQDiscoveryHandler() throws SocketException, UnknownHostException {
    }

    @ServiceActivator(inputChannel = StageLinQChannelID)
    public void handleMessage(Message message) throws JsonProcessingException {

        if (!OSTConfiguration.isFrontEndRunning()){
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
