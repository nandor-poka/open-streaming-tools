package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.config.OSTConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class BeatInfoServiceTCPSocketServerConfiguration {

    private final int socketPort = OSTConfiguration.BEATINFO_SERVICE_PORT;
    public static final BeatInfoMessageSerializer SERIALIZER = new BeatInfoMessageSerializer();
    /**
     * Reply messages are routed to the connection only if the reply contains the ip_connectionId header
     * that was inserted into the original message by the connection factory.
     */
    @MessagingGateway(defaultRequestChannel = "toBeatInfo")
    public interface Gateway {
        void send(String message, @Header(IpHeaders.CONNECTION_ID) String connectionId);
    }

    @Bean
    public MessageChannel toBeatInfo() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fromBeatInfo() {
        return new DirectChannel();
    }

    @Bean
    public AbstractServerConnectionFactory BeatInfoServiceServerCF() {
        TcpNetServerConnectionFactory beatInfoServerCf = new TcpNetServerConnectionFactory(socketPort);
        beatInfoServerCf.setSerializer(SERIALIZER);
        beatInfoServerCf.setDeserializer(SERIALIZER);
        beatInfoServerCf.setSoTcpNoDelay(true);
        beatInfoServerCf.setSoKeepAlive(true);
        return beatInfoServerCf;
    }

    @Bean
    public AbstractClientConnectionFactory BeatInfoServiceClientCF() {

        TcpNetClientConnectionFactory beatInfoClientCf = new TcpNetClientConnectionFactory("localhost", socketPort);
        beatInfoClientCf.setSerializer(SERIALIZER);
        beatInfoClientCf.setDeserializer(SERIALIZER);
        beatInfoClientCf.setSoTcpNoDelay(true);
        beatInfoClientCf.setSoKeepAlive(true);
        return beatInfoClientCf;
    }

    @Bean
    public TcpInboundGateway BeatInfoTcpInGate() {
        TcpInboundGateway beatInfoServiceTCPInGate = new TcpInboundGateway();
        beatInfoServiceTCPInGate.setConnectionFactory(BeatInfoServiceServerCF());
        beatInfoServiceTCPInGate.setRequestChannel(toBeatInfo());
        beatInfoServiceTCPInGate.setReplyChannel(fromBeatInfo());
        return beatInfoServiceTCPInGate;
    }

    @Bean
    public TcpOutboundGateway BeatInfoTcpOutGate() {
        TcpOutboundGateway beatInfoServiceTCPOutgate = new TcpOutboundGateway();
        beatInfoServiceTCPOutgate.setConnectionFactory(BeatInfoServiceClientCF());
        beatInfoServiceTCPOutgate.setReplyChannel(fromBeatInfo());
        return beatInfoServiceTCPOutgate;
    }

}
