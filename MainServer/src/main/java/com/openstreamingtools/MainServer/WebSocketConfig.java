package com.openstreamingtools.MainServer;


import com.openstreamingtools.MainServer.udp.StageLinQDiscoveryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.ip.udp.MulticastReceivingChannelAdapter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.openstreamingtools.MainServer.config.Configuration.WEBSOCKET_CONNECTION_PATH;
import static com.openstreamingtools.MainServer.config.Configuration.WEBSOCKET_DATA_PATH;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public MulticastReceivingChannelAdapter udpIn() {
        MulticastReceivingChannelAdapter  adapter = new MulticastReceivingChannelAdapter ("239.255.255.250",51337);
        adapter.setOutputChannelName(StageLinQDiscoveryHandler.StageLinQChannelID);
        return adapter;
    }

    private TaskScheduler messageBrokerTaskScheduler;

    @Autowired
    public void setMessageBrokerTaskScheduler(@Lazy TaskScheduler taskScheduler) {
        this.messageBrokerTaskScheduler = taskScheduler;
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(WEBSOCKET_DATA_PATH).setHeartbeatValue(new long[] {10000, 20000})
                .setTaskScheduler(this.messageBrokerTaskScheduler);;
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKET_CONNECTION_PATH).setAllowedOrigins("*");
        registry.addEndpoint(WEBSOCKET_CONNECTION_PATH).setAllowedOrigins("*").withSockJS();
    }
}