package com.openstreamingtools.MainServer.twitch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.messaging.TwitchEventBroadcaster;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static com.openstreamingtools.MainServer.twitch.TwitchUtils.TWITCH_SUBSCRIBE;

/**
 * Twitch WebSocket client for Broadcaster user
 * Handles channel points and broadcaster-related events
 */
@Component
@Slf4j
public class BroadcasterTwitchWebSocketClient extends BaseTwitchWebSocketClient {

    @Autowired
    public BroadcasterTwitchWebSocketClient(TwitchEventBroadcaster eventBroadcaster) {
        super("BROADCASTER");
        setEventBroadcaster(eventBroadcaster);
    }

    @Override
    protected boolean shouldConnect() {
        return OSTConfiguration.settings.getTwitchBroadcasterToken() != null &&
               OSTConfiguration.settings.getTwitchUser() != null &&
               OSTConfiguration.settings.isBroadcasterTokenRefreshSuccess();
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

        log.info("📋 {} SUBSCRIBING TO BROADCASTER EVENTS - Session ID: {}", clientType, sessionId);

        String broadcasterToken = OSTConfiguration.settings.getTwitchBroadcasterToken().getAccess_token();
        log.info("🎯 Subscribing to channel events for broadcaster channel: {}", OSTConfiguration.settings.getTwitchUser().getLogin());

        String[] channelSubscriptions = {
                "channel.channel_points_custom_reward_redemption.add",
                "channel.channel_points_automatic_reward_redemption.add",
                "stream.online",
                "stream.offline"
        };
        TwitchSubscriptionTransport transport = new TwitchSubscriptionTransport(sessionId);
        log.debug("🔑 Using broadcaster token for channel subscriptions (length: {})", broadcasterToken.length());
        TwitchSubscribtionCondition channelCondition = new TwitchChatMessageSubscribeCondition(
                OSTConfiguration.settings.getBotUser().getId());
        String response = null;
        for(String subType : channelSubscriptions){
            TwitchSubscribeMessage subscribeMessage = new TwitchSubscribeMessage();
            subscribeMessage.setType(subType);
            subscribeMessage.setTransport(transport);
            subscribeMessage.setCondition(channelCondition);

            // Channel points subscriptions use broadcaster's channel by default (no condition needed)

            // Log detailed payload for channel points subscription
            try {
                String channelPayloadJson = Utils.objectMapper.writeValueAsString(subscribeMessage);
                log.info("📤 {} SUBSCRIPTION PAYLOAD - Length: {} bytes", subType.toUpperCase(), channelPayloadJson.length());
                log.debug("📄 {} subscription payload: {}", subType, channelPayloadJson);
                log.debug("   └─ Type: {}", subscribeMessage.getType());
                log.debug("   └─ Transport: Session ID {}", transport.getSession_id());
                log.debug("   └─ Condition: {}", channelCondition);
            } catch (JsonProcessingException e) {
                log.warn("⚠️ Could not serialize {} subscription payload for logging: {}", subType, e.getMessage());
            }

            log.debug("📡 Creating subscription for: {}", subType);
            response = Utils.restClient.post()
                    .uri(TWITCH_SUBSCRIBE)
                    .header("Authorization","Bearer " + broadcasterToken)
                    .header("Client-Id", OSTConfiguration.getTWITCH_CLIEND_ID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(subscribeMessage)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, resp) -> {
                        log.error("❌ Failed to subscribe to {}: HTTP {} - {}", subType, resp.getStatusCode(), resp.getStatusText());
                    })
                    .body(String.class);
            log.info("✅ {} subscription response: {}", subType, response);
        }

        // Broadcast subscription status to frontend
        if (eventBroadcaster != null) {
            eventBroadcaster.broadcastSubscriptionStatus(clientType + "_custom_rewards", "active");
            eventBroadcaster.broadcastSubscriptionStatus(clientType + "_automatic_rewards", "active");
            eventBroadcaster.broadcastSubscriptionStatus(clientType + "_stream_online", "active");
            eventBroadcaster.broadcastSubscriptionStatus(clientType + "_stream_offline", "active");
            log.debug("📡 {} broadcasted subscription statuses: active", clientType);
        }

    }
}
