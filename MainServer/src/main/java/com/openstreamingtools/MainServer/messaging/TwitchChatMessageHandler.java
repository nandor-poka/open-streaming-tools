package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles processing of Twitch chat messages and commands
 * Processes commands on the backend and propagates raw messages to frontend
 */
@Service
@Slf4j
public class TwitchChatMessageHandler {

    private final TwitchEventBroadcaster eventBroadcaster;

    @Autowired
    public TwitchChatMessageHandler(TwitchEventBroadcaster eventBroadcaster) {
        this.eventBroadcaster = eventBroadcaster;
    }

    /**
     * Process incoming chat message from Twitch
     * Handles commands and propagates raw message to frontend
     *
     * @param eventPayload The chat message event from Twitch
     */
    public void processChatMessage(JsonNode eventPayload) {
        try {
            String messageText = eventPayload.get("message") != null ?
                eventPayload.get("message").get("text").asText() : "";

            String chatterLogin = eventPayload.get("chatter_user_login") != null ?
                eventPayload.get("chatter_user_login").asText() : "unknown";

            log.info("💬 PROCESSING CHAT MESSAGE - User: {}, Message: '{}', Full Payload: {}",
                chatterLogin, messageText, eventPayload.toString());

            // Process chat commands
            processChatCommands(messageText, eventPayload);

            // Broadcast raw message to frontend
            eventBroadcaster.broadcastChatMessage(eventPayload);

        } catch (Exception e) {
            log.error("❌ ERROR processing chat message: {}", e.getMessage(), e);
            eventBroadcaster.broadcastError("Failed to process chat message: " + e.getMessage());
        }
    }

    /**
     * Process chat commands (e.g., !recommend, !shoutout, etc.)
     *
     * @param messageText The full message text
     * @param eventPayload The full event payload
     */
    private void processChatCommands(String messageText, JsonNode eventPayload) {
        try {
            log.debug("🔍 CHECKING FOR COMMANDS - Message: '{}'", messageText);

            // Handle !recommend command
            if (messageText.trim().equalsIgnoreCase("!recommend")) {
                log.info("🎵 RECOMMEND COMMAND DETECTED - Processing !recommend");
                handleRecommendCommand(eventPayload);
            }
            // Handle !shoutout command
            else if (messageText.trim().startsWith("!shoutout ") || messageText.trim().startsWith("!so ")) {
                log.info("📣 SHOUTOUT COMMAND DETECTED - Processing shoutout: {}", messageText);
                handleShoutoutCommand(messageText);
            }
            // Add more commands here as needed
            else {
                log.debug("ℹ️ NO COMMAND DETECTED - Regular chat message");
            }

        } catch (Exception e) {
            log.error("❌ ERROR processing chat commands: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle !recommend command
     * Gets a song recommendation in the current key
     */
    private void handleRecommendCommand(JsonNode eventPayload) {
        try {
            log.info("🎵 EXECUTING RECOMMEND COMMAND - Broadcasting to frontend for song recommendation");
            // This will be handled by frontend listening to a dedicated topic
            // Backend just broadcasts the command request
            eventBroadcaster.broadcastChatCommand("recommend", eventPayload);
            log.info("✅ RECOMMEND COMMAND PROCESSED - Broadcasted to frontend");
        } catch (Exception e) {
            log.error("❌ ERROR handling recommend command: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle !shoutout or !so command
     * Sends a shoutout message to Twitch chat
     */
    private void handleShoutoutCommand(String messageText) {
        try {
            String targetUser = messageText.replaceAll("^!(?:shoutout|so)\\s+", "").trim();
            if (targetUser.isEmpty()) {
                log.warn("⚠️ SHOUTOUT COMMAND INVALID - No target user specified");
                return;
            }

            // Remove @ prefix if present
            if (targetUser.startsWith("@")) {
                targetUser = targetUser.substring(1);
            }

            log.info("📣 EXECUTING SHOUTOUT COMMAND - Target: {}, Sending to Twitch chat", targetUser);
            TwitchUtils.sendToChat("/shoutout " + targetUser);
            log.info("✅ SHOUTOUT COMMAND PROCESSED - Sent to Twitch chat for user: {}", targetUser);

        } catch (Exception e) {
            log.error("❌ ERROR handling shoutout command: {}", e.getMessage(), e);
        }
    }
}
