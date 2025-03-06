package com.openstreamingtools.MainServer.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.openstreamingtools.MainServer.config.OSTConfiguration.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {



    private TaskScheduler messageBrokerTaskScheduler;

    @Autowired
    public void setMessageBrokerTaskScheduler(@Lazy TaskScheduler messageBrokerTaskScheduler) {
        this.messageBrokerTaskScheduler = messageBrokerTaskScheduler;
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(WEBSOCKET_DATA_PATH).setHeartbeatValue(new long[] {10000, 20000})
                .setTaskScheduler(this.messageBrokerTaskScheduler);
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKET_CONNECTION_PATH).setAllowedOrigins(FRONTEND_ORIGN);
        registry.addEndpoint(WEBSOCKET_CONNECTION_PATH).setAllowedOrigins(FRONTEND_ORIGN).withSockJS();
        registry.addEndpoint(WEBSOCKET_CONNECTION_PATH).setAllowedOrigins(FRONTEND_JAR_ORIGN);
        registry.addEndpoint(WEBSOCKET_CONNECTION_PATH).setAllowedOrigins(FRONTEND_JAR_ORIGN).withSockJS();
    }
}