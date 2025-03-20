package com.openstreamingtools.MainServer.websocket;


import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import com.openstreamingtools.MainServer.twitch.TwitchWebsocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
@Slf4j
public class OSTStompSessionHandler implements StompSessionHandler {
    private String websocketSessionID;
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        TwitchUtils.subscribeToTwitch(websocketSessionID);
        OSTConfiguration.settings.setTwitchStatus(true);
        OSTConfiguration.saveSettings();

    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
    log.error(exception.toString());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error(exception.toString());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TwitchWebsocketMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        TwitchWebsocketMessage msg = (TwitchWebsocketMessage) payload;
        log.debug(msg.toString());
        switch (msg.getMetadata().getMessage_type()){
            case "session_welcome":
                websocketSessionID = msg.getPayload().getSession().getId();
                log.debug("websock session {}",websocketSessionID);
                break;
            case "notification":
                log.debug(msg.toString());
                break;
        }
    }
}
