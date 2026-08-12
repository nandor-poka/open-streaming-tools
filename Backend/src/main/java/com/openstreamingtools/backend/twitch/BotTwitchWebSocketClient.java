package com.openstreamingtools.backend.twitch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.config.OSTConfiguration;
import com.openstreamingtools.backend.messaging.TwitchChatMessageHandler;
import com.openstreamingtools.backend.messaging.TwitchEventBroadcaster;
import com.openstreamingtools.backend.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Twitch WebSocket client for Bot user
 * Handles chat message subscriptions and bot-related events
 */
@Component
@Slf4j
public class BotTwitchWebSocketClient extends BaseTwitchWebSocketClient {

    @Autowired
    public BotTwitchWebSocketClient(TwitchEventBroadcaster eventBroadcaster) {
        super(UserType.BOT);
        setEventBroadcaster(eventBroadcaster);
        setChatMessageHandler(new TwitchChatMessageHandler(eventBroadcaster));
    }

    @Override
    protected boolean shouldConnect() {
        return OSTConfiguration.settings.getTwitchBotToken() != null &&
               OSTConfiguration.settings.getBotUser() != null &&
               OSTConfiguration.settings.isBotTokenRefreshSuccess();
    }

    @Override
    protected void subscribeToEvents() {
        if (sessionId == null) {
            log.warn("⚠️ {} Session ID not available yet, cannot subscribe to events", clientType);
            return;
        }

        if (!shouldConnect()) {
            log.warn("⚠️ {} Not subscribing to events - token or user not available", clientType);
            return;
        }

        log.info("📋 {} SUBSCRIBING TO BOT EVENTS - Session ID: {}", clientType, sessionId);

        try {
            // Subscribe to chat messages for bot's channel
            TwitchSubscribeMessage chatSubscribeMessage = new TwitchSubscribeMessage();
            chatSubscribeMessage.setType("channel.chat.message");
            chatSubscribeMessage.setTransport(new TwitchSubscriptionTransport(sessionId));
            chatSubscribeMessage.setCondition(new TwitchChatMessageSubscribeCondition(
                OSTConfiguration.settings.getBotUser().getId()));

            String chatToken = OSTConfiguration.settings.getTwitchBotToken().getAccess_token();

            // Log detailed payload for chat subscription
            String chatPayloadJson = Utils.objectMapper.writeValueAsString(chatSubscribeMessage);
            log.info("📤 {} CHAT SUBSCRIPTION PAYLOAD - Length: {} bytes", clientType, chatPayloadJson.length());
            log.debug("📄 {} Chat subscription payload: {}", clientType, chatPayloadJson);

            String response = Utils.restClient.post()
                .uri(TwitchUtils.TWITCH_SUBSCRIBE)
                .header("Authorization","Bearer " + chatToken)
                .header("Client-Id", OSTConfiguration.getTWITCH_CLIEND_ID())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(chatSubscribeMessage)
                .retrieve()
                .onStatus(org.springframework.http.HttpStatusCode::is4xxClientError, (request, resp) -> {
                    log.error("❌ {} Failed to subscribe to chat messages: HTTP {} - {}", clientType, resp.getStatusCode(), resp.getStatusText());
                })
                .body(String.class);

            log.info("✅ {} Chat message subscription response: {}", clientType, response);

            // Broadcast subscription status to frontend
            if (eventBroadcaster != null) {
                eventBroadcaster.broadcastSubscriptionStatus(clientType + "_chat", "active");
                log.debug("📡 {} broadcasted chat subscription status: active", clientType);
            }

        } catch (JsonProcessingException e) {
            log.error("❌ {} Error creating chat subscription: {}", clientType, e.getMessage(), e);
        }
    }
}
