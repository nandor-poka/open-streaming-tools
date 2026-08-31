package com.openstreamingtools.backend.websocket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.openstreamingtools.backend.config.OSTConfiguration.WEBSOCKET_CONNECTION_PATH;
import static com.openstreamingtools.backend.config.OSTConfiguration.WEBSOCKET_DATA_PATH;

/**
 * Spring configuration class for STOMP WebSocket message broker setup.
 * Configures the simple message broker with heartbeat settings and enables
 * WebSocket connections with SockJS fallback support.
 *
 * <p>Configures:
 * <ul>
 *   <li>Simple message broker with configurable heartbeat intervals</li>
 *   <li>STOMP endpoints for WebSocket connections</li>
 *   <li>Application destination prefixes for messaging</li>
 *   <li>SockJS fallback for browsers without native WebSocket support</li>
 * </ul>
 *
 * @see EnableWebSocketMessageBroker
 * @see WebSocketMessageBrokerConfigurer
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketServerConfig implements WebSocketMessageBrokerConfigurer {



    private TaskScheduler messageBrokerTaskScheduler;

    /**
     * Injects the message broker task scheduler used by the STOMP broker.
     *
     * @param messageBrokerTaskScheduler the TaskScheduler to use for the message broker
     */
    @Autowired
    public void setMessageBrokerTaskScheduler(@Lazy TaskScheduler messageBrokerTaskScheduler) {
        this.messageBrokerTaskScheduler = messageBrokerTaskScheduler;
    }

    /**
     * Configures the message broker with heartbeat settings and destination prefix.
     * Enables a simple broker for the {@link OSTConfiguration#WEBSOCKET_DATA_PATH} with
     * heartbeat intervals of 10 and 20 seconds.
     *
     * @param config the MessageBrokerRegistry to configure
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(WEBSOCKET_DATA_PATH).setHeartbeatValue(new long[] {10000, 20000})
                .setTaskScheduler(this.messageBrokerTaskScheduler);
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers STOMP endpoints for WebSocket connections.
     * Supports both raw WebSocket and SockJS fallback protocols with CORS enabled for all origins.
     *
     * @param registry the StompEndpointRegistry to register endpoints
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKET_CONNECTION_PATH).setAllowedOrigins("*");
        registry.addEndpoint(WEBSOCKET_CONNECTION_PATH).setAllowedOrigins("*").withSockJS();

    }
}