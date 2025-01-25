package com.openstreamingtools.MainServer.tcp;


import com.openstreamingtools.MainServer.utils.Utils;
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
public class DirectoryServiceTCPSocketServerConfiguration {



    private final int socketPort = Utils.DIRECTORY_SERVICE_PORT;
    public static final DirectoryMessageSerializer SERIALIZER = new DirectoryMessageSerializer();

    /**
     * Reply messages are routed to the connection only if the reply contains the ip_connectionId header
     * that was inserted into the original message by the connection factory.
     */
    @MessagingGateway(defaultRequestChannel = "toDirectory")
    public interface Gateway {
        void send(String message, @Header(IpHeaders.CONNECTION_ID) String connectionId);
    }

    @Bean
    public MessageChannel toDirectory() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fromDirectory() {
        return new DirectChannel();
    }

    @Bean
    public AbstractServerConnectionFactory DirectoryServiceServerCF() {
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
    public AbstractClientConnectionFactory DirectoryServiceClientCF() {

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
        inGate.setConnectionFactory(DirectoryServiceServerCF());
        inGate.setRequestChannel(toDirectory());
        inGate.setReplyChannel(fromDirectory());
        return inGate;
    }

    @Bean
    public TcpOutboundGateway tcpOutGate() {
        TcpOutboundGateway outGate = new TcpOutboundGateway();
        outGate.setConnectionFactory(DirectoryServiceClientCF());
        outGate.setReplyChannel(fromDirectory());
        return outGate;
    }



}
