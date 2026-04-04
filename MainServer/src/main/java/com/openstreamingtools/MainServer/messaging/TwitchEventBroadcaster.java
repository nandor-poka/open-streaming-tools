package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Broadcasts Twitch events to connected frontend clients via WebSocket
 */
@Component
@Slf4j
public class TwitchEventBroadcaster {

    private static final String TWITCH_BASE_TOPIC = "/api/websocketData/twitch";
    private static final String TWITCH_SESSION_TOPIC = TWITCH_BASE_TOPIC + "/session";
    private static final String TWITCH_CHAT_TOPIC = TWITCH_BASE_TOPIC + "/chat";
    private static final String TWITCH_CHAT_COMMAND_TOPIC = TWITCH_BASE_TOPIC + "/chat/command";
    private static final String TWITCH_POINTS_TOPIC = TWITCH_BASE_TOPIC + "/points";
    private static final String TWITCH_STATUS_TOPIC = TWITCH_BASE_TOPIC + "/status";
    private static final String TWITCH_ERROR_TOPIC = TWITCH_BASE_TOPIC + "/error";

    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper;

    @Autowired
    public TwitchEventBroadcaster(SimpMessagingTemplate template) {
        this.template = template;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Broadcast session welcome event
     */
    public void broadcastSessionWelcome(String sessionId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "session_welcome");
            message.put("sessionId", sessionId);
            message.put("timestamp", System.currentTimeMillis());

            String messageJson = objectMapper.writeValueAsString(message);
            template.convertAndSend(TWITCH_SESSION_TOPIC, messageJson);
            log.info("📤 BROADCAST SESSION WELCOME - Topic: {}, Payload: {}", TWITCH_SESSION_TOPIC, messageJson);
        } catch (Exception e) {
            log.error("❌ ERROR broadcasting session welcome: {}", e.getMessage(), e);
        }
    }

    /**
     * Broadcast Twitch events based on subscription type
     */
    public void broadcastTwitchEvent(String subscriptionType, JsonNode eventPayload) {
        try {
            log.info("🎯 BROADCASTING TWITCH EVENT - Type: {}, Payload: {}", subscriptionType, eventPayload != null ? eventPayload.toString() : "null");

            switch (subscriptionType) {
                case "channel.chat.message":
                    broadcastChatMessage(eventPayload);
                    break;
                case "channel.channel_points_custom_reward_redemption.add":
                case "channel.channel_points_automatic_reward_redemption.add":
                    broadcastPointsRedemption(eventPayload);
                    break;
                default:
                    log.warn("❓ UNHANDLED SUBSCRIPTION TYPE: {} - Payload: {}", subscriptionType, eventPayload);
            }
        } catch (Exception e) {
            log.error("❌ ERROR broadcasting Twitch event: {}", e.getMessage(), e);
        }
    }

    /**
     * Broadcast chat message event
     */
    public void broadcastChatMessage(JsonNode eventPayload) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "channel.chat.message");
            message.put("event", eventPayload);
            message.put("timestamp", System.currentTimeMillis());

            String messageJson = objectMapper.writeValueAsString(message);
            template.convertAndSend(TWITCH_CHAT_TOPIC, messageJson);
            log.info("💬 BROADCAST CHAT MESSAGE - Topic: {}, Payload: {}", TWITCH_CHAT_TOPIC, messageJson);
        } catch (Exception e) {
            log.error("❌ ERROR broadcasting chat message: {}", e.getMessage(), e);
        }
    }

    /**
     * Broadcast channel points redemption event
     */
    private void broadcastPointsRedemption(JsonNode eventPayload) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "channel.channel_points_redemption");
            message.put("event", eventPayload);
            message.put("timestamp", System.currentTimeMillis());

            String messageJson = objectMapper.writeValueAsString(message);
            template.convertAndSend(TWITCH_POINTS_TOPIC, messageJson);
            log.info("🎁 BROADCAST POINTS REDEMPTION - Topic: {}, Payload: {}", TWITCH_POINTS_TOPIC, messageJson);
        } catch (Exception e) {
            log.error("❌ ERROR broadcasting points redemption: {}", e.getMessage(), e);
        }
    }

    /**
     * Broadcast connection status
     */
    public void broadcastConnectionStatus(String status) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("status", status);
            message.put("timestamp", System.currentTimeMillis());

            String statusString = objectMapper.writeValueAsString(message);
            template.convertAndSend(TWITCH_STATUS_TOPIC, statusString);
            log.info("🔗 BROADCAST CONNECTION STATUS - Topic: {}, Payload: {}", TWITCH_STATUS_TOPIC, statusString);
        } catch (Exception e) {
            log.error("❌ ERROR broadcasting connection status: {}", e.getMessage(), e);
        }
    }

    /**
     * Broadcast error message
     */
    public void broadcastError(String errorMessage) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("error", errorMessage);
            message.put("timestamp", System.currentTimeMillis());

            String messageJson = objectMapper.writeValueAsString(message);
            template.convertAndSend(TWITCH_ERROR_TOPIC, messageJson);
            log.info("🚨 BROADCAST ERROR - Topic: {}, Payload: {}", TWITCH_ERROR_TOPIC, messageJson);
        } catch (Exception e) {
            log.error("❌ ERROR broadcasting error message: {}", e.getMessage(), e);
        }
    }

    /**
     * Broadcast chat command to frontend for processing
     * Separate from raw messages to allow frontend to react to specific commands
     */
    public void broadcastChatCommand(String commandType, JsonNode eventPayload) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("command", commandType);
            message.put("event", eventPayload);
            message.put("timestamp", System.currentTimeMillis());

            String messageJson = objectMapper.writeValueAsString(message);
            template.convertAndSend(TWITCH_CHAT_COMMAND_TOPIC, messageJson);
            log.info("⚡ BROADCAST CHAT COMMAND - Topic: {}, Payload: {}", TWITCH_CHAT_COMMAND_TOPIC, messageJson);
        } catch (Exception e) {
            log.error("❌ ERROR broadcasting chat command: {}", e.getMessage(), e);
        }
    }
}
