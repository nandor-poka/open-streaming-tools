package com.openstreamingtools.MainServer.websocket;

import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Slf4j
@Deprecated
public class WeboscketClient {
    public static WebSocketClient client = new StandardWebSocketClient();

    public static WebSocketStompClient stompClient = new WebSocketStompClient(client);
    public static StompSessionHandler sessionHandler = new OSTStompSessionHandler();

    public static void connect(){
        log.debug("Starting weboscket.");
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connectAsync(TwitchUtils.TWITCH_EVENTSUB_WEBSOCKET_ADDRESS, sessionHandler);
    }

}
