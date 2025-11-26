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
public class StateMapServiceTCPSocketServerConfiguration {

    private final int socketPort = OSTConfiguration.STATEMAP_SERVICE_PORT;
    public static final StateMapMessageSerializer SERIALIZER = new StateMapMessageSerializer();

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


    @Bean
    public AbstractServerConnectionFactory StateMapServiceServerCF() {
        TcpNetServerConnectionFactory stateMapServerCf = new TcpNetServerConnectionFactory(socketPort);
        stateMapServerCf.setSerializer(SERIALIZER);
        stateMapServerCf.setDeserializer(SERIALIZER);
        stateMapServerCf.setSoTcpNoDelay(true);
        stateMapServerCf.setSoKeepAlive(true);
        return stateMapServerCf;
    }

    @Bean
    public AbstractClientConnectionFactory StateMapServiceClientCF() {

        TcpNetClientConnectionFactory sstateMapClientCf = new TcpNetClientConnectionFactory("localhost", socketPort);
        sstateMapClientCf.setSerializer(SERIALIZER);
        sstateMapClientCf.setDeserializer(SERIALIZER);
        sstateMapClientCf.setSoTcpNoDelay(true);
        sstateMapClientCf.setSoKeepAlive(true);
        return sstateMapClientCf;
    }


    @Bean
    public TcpInboundGateway StateMapTcpInGate() {
        TcpInboundGateway StateMapServiceTCPInGate = new TcpInboundGateway();
        StateMapServiceTCPInGate.setConnectionFactory(StateMapServiceServerCF());
        StateMapServiceTCPInGate.setRequestChannel(toStateMap());
        StateMapServiceTCPInGate.setReplyChannel(fromStateMap());
        return StateMapServiceTCPInGate;
    }

    @Bean
    public TcpOutboundGateway StateMapTcpOutGate() {
        TcpOutboundGateway StateMapServiceTCPOutGate = new TcpOutboundGateway();
        StateMapServiceTCPOutGate.setConnectionFactory(StateMapServiceClientCF());
        StateMapServiceTCPOutGate.setReplyChannel(fromStateMap());
        return StateMapServiceTCPOutGate;
    }


}
