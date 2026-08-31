package com.openstreamingtools.backend.tcp;

import com.openstreamingtools.backend.config.OSTConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.inbound.TcpInboundGateway;
import org.springframework.integration.ip.tcp.outbound.TcpOutboundGateway;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Spring Integration configuration for StateMap service TCP socket communication.
 * Sets up bidirectional TCP communication with StageLinQ devices for state map data exchange.
 * Configures client and server connection factories, messaging channels, and gateways for
 * inbound and outbound TCP communication.
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class StateMapServiceTCPSocketServerConfiguration {

    private final int socketPort = OSTConfiguration.STATEMAP_SERVICE_PORT;
    public static final StateMapMessageSerializer SERIALIZER = new StateMapMessageSerializer();

    /**
     * Messaging gateway for sending state map messages.
     * Reply messages are routed to the connection only if the reply contains the ip_connectionId header
     * that was inserted into the original message by the connection factory.
     */
    @MessagingGateway(defaultRequestChannel = "toStateMap")
    public interface Gateway {
        /**
         * Sends a message to the state map service.
         *
         * @param message the message content
         * @param connectionId the TCP connection ID
         */
        void send(String message, @Header(IpHeaders.CONNECTION_ID) String connectionId);
    }

    @Bean
    public MessageChannel toStateMap() {
        return new DirectChannel();
    }

    /**
     * Creates the messaging channel for receiving state map data from the TCP server.
     *
     * @return DirectChannel for inbound state map messages
     */
    @Bean
    public MessageChannel fromStateMap() {
        return new DirectChannel();
    }


    /**
     * Creates the TCP server connection factory for state map service communication.
     * Listens on the configured StateMap service port.
     *
     * @return server connection factory with serializers configured
     */
    @Bean
    public AbstractServerConnectionFactory StateMapServiceServerCF() {
        TcpNetServerConnectionFactory stateMapServerCf = new TcpNetServerConnectionFactory(socketPort);
        stateMapServerCf.setSerializer(SERIALIZER);
        stateMapServerCf.setDeserializer(SERIALIZER);
        stateMapServerCf.setSoTcpNoDelay(true);
        stateMapServerCf.setSoKeepAlive(true);
        return stateMapServerCf;
    }

    /**
     * Creates the TCP client connection factory for state map service communication.
     * Connects to local host on the configured StateMap service port.
     *
     * @return client connection factory with serializers configured
     */
    @Bean
    public AbstractClientConnectionFactory StateMapServiceClientCF() {

        TcpNetClientConnectionFactory stateMapClientCf = new TcpNetClientConnectionFactory("localhost", socketPort);
        stateMapClientCf.setSerializer(SERIALIZER);
        stateMapClientCf.setDeserializer(SERIALIZER);
        stateMapClientCf.setSoTcpNoDelay(true);
        stateMapClientCf.setSoKeepAlive(true);
        return stateMapClientCf;
    }


    /**
     * Creates the TCP inbound gateway for receiving state map messages.
     *
     * @return configured TcpInboundGateway
     */
    @Bean
    public TcpInboundGateway StateMapTcpInGate() {
        TcpInboundGateway StateMapServiceTCPInGate = new TcpInboundGateway();
        StateMapServiceTCPInGate.setConnectionFactory(StateMapServiceServerCF());
        StateMapServiceTCPInGate.setRequestChannel(toStateMap());
        StateMapServiceTCPInGate.setReplyChannel(fromStateMap());
        return StateMapServiceTCPInGate;
    }

    /**
     * Creates the TCP outbound gateway for sending state map messages.
     *
     * @return configured TcpOutboundGateway
     */
    @Bean
    public TcpOutboundGateway StateMapTcpOutGate() {
        TcpOutboundGateway StateMapServiceTCPOutGate = new TcpOutboundGateway();
        StateMapServiceTCPOutGate.setConnectionFactory(StateMapServiceClientCF());
        StateMapServiceTCPOutGate.setReplyChannel(fromStateMap());
        return StateMapServiceTCPOutGate;
    }


}
