package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.utils.Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.*;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

public class StatemapServiceTCPSocketServerConfiguration {


    private final int socketPort = Utils.STATEMAP_SERVICE_PORT;
    public static final ByteArrayLengthHeaderSerializer SERIALIZER = new ByteArrayLengthHeaderSerializer();
    public static final String NEW_STATEMAP_CONNECTION_CHANNEL_NAME = "new StateMap connection";
    //serializer is headerbytearray

    /**
     * Reply messages are routed to the connection only if the reply contains the ip_connectionId header
     * that was inserted into the original message by the connection factory.
     */
    @MessagingGateway(defaultRequestChannel = "toStateMap")
    public interface Gateway {
        void send(String message, @Header(IpHeaders.CONNECTION_ID) String connectionId);
    }

    @Bean
    public MessageChannel toStateMap() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fromStateMap() {
        return new DirectChannel();
    }

    @Bean(NEW_STATEMAP_CONNECTION_CHANNEL_NAME)
    public MessageChannel toStateMapNewConnection(){
        return new DirectChannel();
    }
    @Bean
    public AbstractServerConnectionFactory StateMapServiceServerCF() {
        TcpNetServerConnectionFactory serverCf = new TcpNetServerConnectionFactory(socketPort);
        serverCf.setSerializer(SERIALIZER);
        serverCf.setDeserializer(SERIALIZER);
        serverCf.setSoTcpNoDelay(true);
        serverCf.setSoKeepAlive(true);
        // serverCf.setSingleUse(true);
        // final int soTimeout = 5000;
        // serverCf.setSoTimeout(soTimeout);
        return serverCf;
    }

    @Bean
    public AbstractClientConnectionFactory StateMapServiceClientCF() {

        TcpNetClientConnectionFactory clientCf = new TcpNetClientConnectionFactory("localhost", socketPort);
        clientCf.setSerializer(SERIALIZER);
        clientCf.setDeserializer(SERIALIZER);
        clientCf.setSoTcpNoDelay(true);
        clientCf.setSoKeepAlive(true);
        // clientCf.setSingleUse(true);
        // final int soTimeout = 5000;
        // clientCf.setSoTimeout(soTimeout);
        return clientCf;
    }


    @Bean
    public TcpInboundGateway tcpInGate() {
        TcpInboundGateway inGate = new TcpInboundGateway();
        inGate.setConnectionFactory(StateMapServiceServerCF());
        inGate.setRequestChannel(toStateMap());
        inGate.setReplyChannel(fromStateMap());
        return inGate;
    }

    @Bean
    public TcpOutboundGateway tcpOutGate() {
        TcpOutboundGateway outGate = new TcpOutboundGateway();
        outGate.setConnectionFactory(StateMapServiceClientCF());
        outGate.setReplyChannel(fromStateMap());
        return outGate;
    }


}
