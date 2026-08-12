package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class DirectoryMessage {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryMessage.class);

    private int messageId;
    private UUID deviceId;
    private long timepstamp;
    private String serivceName;
    private int servicePort;

    public DirectoryMessage(int messageId) {
       this.messageId = messageId;
    }

    public DirectoryMessage(int messageId, UUID deviceId) {
        this.messageId = messageId;
        this.deviceId = deviceId;
    }

    public DirectoryMessage(int messageId, UUID deviceId, long timepstamp) {
        this.messageId = messageId;
        this.deviceId = deviceId;
        this.timepstamp = timepstamp;
    }

    public DirectoryMessage(int messageId, UUID deviceId, String serivceName, int servicePort) {
        this.messageId = messageId;
        this.deviceId = deviceId;
        this.serivceName = serivceName;
        this.servicePort = servicePort;
    }

    public static DirectoryMessage parseMessage(byte[] message){
        //quick fail if message is too short
        if (message.length < 20){
            logger.debug("Directory message is shorter than 20 bytes, ignoring");
            return new DirectoryMessage(-1);
        }
        int messageId = parseMessageId(message);
        ByteBuffer bb = ByteBuffer.wrap(Arrays.copyOfRange(message, 4, 20));
        long high = bb.getLong();
        long low = bb.getLong();
        UUID deviceId = new UUID(high,low);
        switch (messageId){
            case DirectoryService.SERVICE_REQUEST:
                return new DirectoryMessage(messageId, deviceId);
            case DirectoryService.SERVICE_ANNOUNCEMENT:
                if (message.length < 26){
                    logger.debug("Directory announcement message is shorter than 26 bytes, ignoring");
                    return new DirectoryMessage(-1);
                }
                String serviceName = new String(Arrays.copyOfRange(message, 20, 24));
                int servicePort = new BigInteger(Arrays.copyOfRange(message, 24, 26)).intValue();
                return new DirectoryMessage(messageId, deviceId, serviceName, servicePort);
            case DirectoryService.TIMESTAMP:
                if (message.length < 44){
                    logger.debug("Directory timestamp message is shorter than 44 bytes, ignoring");
                    return new DirectoryMessage(-1);
                }
                long timestamp =  new BigInteger(Arrays.copyOfRange(message, 36, 44)).longValue();
                return new DirectoryMessage(messageId, deviceId, timestamp);
            default:
                logger.debug("Directory message id is invalid, ignoring");
                return new DirectoryMessage(-1);
        }
    }

    public static int parseMessageId (byte[] byteArray){
        return new BigInteger(Arrays.copyOfRange(byteArray, 0, 4)).intValue();
    }



    @Override
    public String toString() {
        return "DirectoryMessage{" +
                "messageId=" + messageId +
                ", deviceId=" + deviceId +
                ", timepstamp=" + timepstamp +
                ", serivceName='" + serivceName +
                ", servicePort=" + servicePort +
                '}';
    }



    public int getMessageId() {
        return messageId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public long getTimepstamp() {
        return timepstamp;
    }

    public int getServicePort() {
        return servicePort;
    }

    public String getSerivceName() {
        return serivceName;
    }



}
