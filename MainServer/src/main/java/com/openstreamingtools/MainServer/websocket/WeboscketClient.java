package com.openstreamingtools.MainServer.websocket;

import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.utils.Utils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;


public class WeboscketClient {
    public static WebSocketClient client = new StandardWebSocketClient();

    public static WebSocketStompClient stompClient = new WebSocketStompClient(client);
    public static StompSessionHandler sessionHandler = new OSTStompSessionHandler();

    public static void connect(){
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connectAsync(Utils.TWITCH_EVENTSUB_WEBSOCKET_ADDRESS, sessionHandler);
    }

}
