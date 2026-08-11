package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * Handles messaging between frontend and backend Twitch WebSocket client
 * Note: WebSocket connection now starts automatically on server startup
 */
@Controller
@Slf4j
public class TwitchEventHandler {
/*

    private final TwitchWebSocketClient twitchWebSocketClient;
    private final TwitchEventBroadcaster twitchEventBroadcaster;
    private final TwitchChatMessageHandler chatMessageHandler;

    @Autowired
    public TwitchEventHandler(TwitchWebSocketClient twitchWebSocketClient,
                             TwitchEventBroadcaster twitchEventBroadcaster,
                             TwitchChatMessageHandler chatMessageHandler) {
        this.twitchWebSocketClient = twitchWebSocketClient;
        this.twitchEventBroadcaster = twitchEventBroadcaster;
        this.chatMessageHandler = chatMessageHandler;
        // Inject dependencies into WebSocket client
        this.twitchWebSocketClient.setEventBroadcaster(twitchEventBroadcaster);
        this.twitchWebSocketClient.setChatMessageHandler(chatMessageHandler);
    }

    */
/**
     * Manually trigger Twitch WebSocket connection (for debugging/testing)
     * Note: Connection normally starts automatically on server startup
     *//*

    @SuppressWarnings("unused")
    @MessageMapping("/app/twitch/connect")
    public void handleTwitchConnect() {
        log.info("🔌 Manual Twitch connection request received");
        try {
            twitchWebSocketClient.connect();
            log.info("✅ Manual Twitch connection initiated");
        } catch (Exception e) {
            log.error("❌ Error initiating manual Twitch connection: {}", e.getMessage(), e);
            twitchEventBroadcaster.broadcastError("Failed to connect: " + e.getMessage());
        }
    }

    */
/**
     * Manually disconnect Twitch WebSocket (for debugging/testing)
     *//*

    @SuppressWarnings("unused")
    @MessageMapping("/app/twitch/disconnect")
    public void handleTwitchDisconnect() {
        log.info("🔌 Manual Twitch disconnect request received");
        try {
            twitchWebSocketClient.disconnect();
            log.info("✅ Manual Twitch disconnect completed");
        } catch (Exception e) {
            log.error("❌ Error during manual Twitch disconnect: {}", e.getMessage(), e);
        }
    }

    */
/**
     * Get current Twitch connection status
     *//*

    @SuppressWarnings("unused")
    @MessageMapping("/app/twitch/status")
    public void handleStatusRequest() {
        log.debug("Twitch status request received");
        String status = twitchWebSocketClient.isConnected() ? "connected" : "disconnected";
        String sessionId = twitchWebSocketClient.getSessionId();
        log.info("📊 Twitch status: {}, Session ID: {}", status, sessionId != null ? sessionId : "none");
        twitchEventBroadcaster.broadcastConnectionStatus(status);
    }

    */
/**
     * Manually trigger event subscription (for debugging/testing)
     *//*

    @SuppressWarnings("unused")
    @MessageMapping("/app/twitch/subscribe")
    public void handleSubscribeRequest() {
        log.info("🔄 Manual subscription request received");
        try {
          //  twitchWebSocketClient.subscribeToEvents();
            log.info("✅ Manual subscription request processed");
        } catch (Exception e) {
            log.error("❌ Error during manual subscription: {}", e.getMessage(), e);
            twitchEventBroadcaster.broadcastError("Failed to subscribe: " + e.getMessage());
        }
    }

    */
/**
     * Send a message to Twitch chat
     * Receives message from frontend and routes it to TwitchUtils for sending
     *//*

    @SuppressWarnings("unused")
    @MessageMapping("/app/twitch/send-message")
    public void handleSendMessage(@Payload String payload) {
        try {
            log.info("💬 Chat message send request received from frontend");
            JsonNode messageNode = Utils.objectMapper.readTree(payload);
            String messageText = messageNode.get("message").asText();

            if (messageText == null || messageText.trim().isEmpty()) {
                log.warn("⚠️ Empty message received, ignoring");
                return;
            }

            log.debug("📤 Sending message to Twitch chat: {}", messageText);
            TwitchUtils.sendToChat(messageText);
            log.info("✅ Chat message sent successfully");

        } catch (Exception e) {
            log.error("❌ Error sending chat message: {}", e.getMessage(), e);
            twitchEventBroadcaster.broadcastError("Failed to send message: " + e.getMessage());
        }
    }
*/

}
