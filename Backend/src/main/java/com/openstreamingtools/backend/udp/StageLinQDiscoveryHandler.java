package com.openstreamingtools.backend.udp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.dj.UnitUtils;
import com.openstreamingtools.backend.dj.stagelinq.DenonUnit;
import com.openstreamingtools.backend.dj.stagelinq.ModelCode;
import com.openstreamingtools.backend.dj.stagelinq.StageLinQAction;
import com.openstreamingtools.backend.messages.stagelinqmessages.ServerDiscoveryMessage;
import com.openstreamingtools.backend.messages.stagelinqmessages.StageLinQDiscoveryMessage;
import com.openstreamingtools.backend.messaging.MessageSender;
import com.openstreamingtools.backend.services.stagelinq.DirectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;

import static com.openstreamingtools.backend.config.OSTConfiguration.STAGELINQ_BORADCAST_IP;
import static com.openstreamingtools.backend.config.OSTConfiguration.STAGELINQ_BROADCAST_PORT;
import static com.openstreamingtools.backend.utils.Utils.objectMapper;


@Service
@Controller
@Component
@Slf4j
public class StageLinQDiscoveryHandler {
   

    public static final String StageLinQChannelID = "StangelinQ Broadcast Discovery";
    DatagramSocket broadcastSocket = new DatagramSocket(STAGELINQ_BROADCAST_PORT, InetAddress.getLocalHost());

    public StageLinQDiscoveryHandler() throws SocketException, UnknownHostException {
    }

    @ServiceActivator(inputChannel = StageLinQChannelID)
    public void handleMessage(Message message) throws JsonProcessingException {
       /*
        if (!OSTConfiguration.isFrontEndRunning()){

            log.debug("Ignoring StageLinQ discovery message because frontend is not running.");
            return;
        }*/

        log.debug("Received StageLinQ discovery message. Payload type={}, headers={}",
                message.getPayload().getClass().getSimpleName(),
                message.getHeaders());

        if (!(message.getPayload() instanceof byte[])) {
            log.warn("Unexpected payload type for discovery message: {}. Expected byte[]",
                    message.getPayload() != null ? message.getPayload().getClass().getName() : "null");
            return;
        }

        Object senderIp = message.getHeaders().get(IpHeaders.IP_ADDRESS);
        log.debug("Discovery message sender IP: {}", senderIp);

        try {
            if (senderIp != null && senderIp.toString().equals(InetAddress.getLocalHost().getHostAddress())) {
                log.debug("Ignoring discovery message from local host ({}).", senderIp);
                return;
            }
        } catch (UnknownHostException e) {
            log.error("Failed to resolve local host while filtering discovery message.", e);
            throw new RuntimeException(e);
        }

        byte[] messageBytes = (byte[]) message.getPayload();
        if (messageBytes.length > 98) { // ignoring discovery messages for offilne analyzer internal model
            return;
        }
        StageLinQDiscoveryMessage disMessage = StageLinQDiscoveryMessage.parse(messageBytes);
        log.debug("Parsed StageLinQ discovery message. modelType={}, modelCode={}, deviceId={}, softwareVersion={}, senderIp={}",
                disMessage.getModelType(), disMessage.getModelCode(), disMessage.getDeviceID(), disMessage.getSoftwareVersion(), senderIp);

        // Retrun if we detect message from unknown model must change later
        if (disMessage.getModelCode().equals(ModelCode.UNKNOWN)){
            log.warn("Ignoring discovery message from unknown model. deviceId={}, senderIp={}",
                    disMessage.getDeviceID(), senderIp);
            return;
        }

        DenonUnit unit = (DenonUnit) UnitUtils.unitMapping.get(disMessage.getModelCode());
        if (unit == null) {
            log.warn("No unit mapping found for modelCode={} (deviceId={}, senderIp={}).", disMessage.getModelCode(), disMessage.getDeviceID(), senderIp);
            return;
        }

        if (disMessage.getAction().equals(StageLinQAction.EXIT)){
            log.info("Received EXIT action from deviceId={}, modelCode={}, senderIp={}. Removing from directory.", disMessage.getDeviceID(), disMessage.getModelCode(), senderIp);
            DirectoryService.removeUnit(disMessage.getDeviceID());
            return;
        }


        unit.setDeviceID(disMessage.getDeviceID());
        unit.setVersion(disMessage.getSoftwareVersion());
        unit.setIpString((String) senderIp);
        log.debug("Updated unit metadata for {}: deviceId={}, ip={}, version={}", unit.getClass().getSimpleName(), unit.getDeviceID(), unit.getIpString(), unit.getVersion());

        if(!DirectoryService.hasUnit(unit.getDeviceID())){
            log.info("Adding new discovered unit to directory: deviceId={}, modelCode={}, ip={}", unit.getDeviceID(), disMessage.getModelCode(), unit.getIpString());
            DirectoryService.addUnit(unit);
        }

        if (DirectoryService.hasUnit(unit.getDeviceID() )
                && !DirectoryService.getUnit(unit.getDeviceID()).acknowledged ){
            log.info("Sending discovery message to frontend for unacknowledged unit {}.", unit.getDeviceID());
            MessageSender.sendMessage(objectMapper.writeValueAsString(disMessage));
        } else if (DirectoryService.hasUnit(unit.getDeviceID())) {
            log.debug("Skipping frontend message for acknowledged unit {}.", unit.getDeviceID());
        }

        ServerDiscoveryMessage broadcastMessage = new ServerDiscoveryMessage();
        DatagramPacket packet = new DatagramPacket(broadcastMessage.getMessageAsBytes(), broadcastMessage.getMessageAsBytes().length);
        packet.setSocketAddress(new InetSocketAddress(STAGELINQ_BORADCAST_IP, STAGELINQ_BROADCAST_PORT));
        log.info("Broadcasting server discovery response to {}:{} for deviceId={}", STAGELINQ_BORADCAST_IP, STAGELINQ_BROADCAST_PORT, unit.getDeviceID());
        try {
            broadcastSocket.setBroadcast(true);
            broadcastSocket.send(packet);
            log.info("Server discovery broadcast succeeded for deviceId={}.", unit.getDeviceID());
        } catch (IOException e) {
            log.error("Failed to broadcast server discovery response to {}:{} for deviceId={}", STAGELINQ_BORADCAST_IP, STAGELINQ_BROADCAST_PORT, unit.getDeviceID(), e);
        }
    }


}
