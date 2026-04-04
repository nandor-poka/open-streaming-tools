package com.openstreamingtools.MainServer.twitch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.messaging.TwitchEventBroadcaster;
import com.openstreamingtools.MainServer.messaging.TwitchChatMessageHandler;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Manages the WebSocket connection to Twitch EventSub
 * Uses NeoVisionaries WebSocket client for advanced features and detailed logging
 * Handles connection lifecycle, session management, heartbeats, and event processing
 */
@Component
@Slf4j
public class TwitchWebSocketClient extends WebSocketAdapter {

    private static final String TWITCH_EVENTSUB_URL = "wss://eventsub.wss.twitch.tv/ws";
    private static final long HEARTBEAT_TIMEOUT_SECONDS = 10;
    private static final long RECONNECT_DELAY_MS = 5000;
    private static final int MAX_RECONNECT_ATTEMPTS = 10;
    private static final long SESSION_WELCOME_TIMEOUT_MS = 30000; // 30 seconds
    private static final int CONNECTION_TIMEOUT_SECONDS = 30;

    private WebSocket twitchWebSocket;
    private String sessionId;
    private ObjectMapper objectMapper;
    private TwitchEventBroadcaster eventBroadcaster;
    private TwitchChatMessageHandler chatMessageHandler;
    private int reconnectAttempts = 0;
    private boolean isConnecting = false;
    private boolean shouldReconnect = true;
    private long connectionStartTime = 0;

    public TwitchWebSocketClient() {
        this.objectMapper = Utils.objectMapper;
        log.debug("✅ TwitchWebSocketClient initialized with NeoVisionaries WebSocket client");
    }

    public void setEventBroadcaster(TwitchEventBroadcaster broadcaster) {
        this.eventBroadcaster = broadcaster;
        log.debug("📡 Event broadcaster set");
    }

    public void setChatMessageHandler(TwitchChatMessageHandler handler) {
        this.chatMessageHandler = handler;
        log.debug("💬 Chat message handler set");
    }

    /**
     * Establish connection to Twitch EventSub WebSocket
     */
    public synchronized void connect() {
        if (isConnecting || twitchWebSocket != null) {
            log.debug("Already connecting or connected to Twitch");
            return;
        }

        if (!OSTConfiguration.settings.isTwitchStatus()) {
            log.warn("⚠️ Twitch is not enabled in settings - skipping WebSocket connection");
            return;
        } else {
            // Re-enable for next reconnect attempt
            shouldReconnect = true;
        }

        isConnecting = true;
        log.info("🔌 Establishing Twitch EventSub WebSocket connection to: {}", TWITCH_EVENTSUB_URL);

        try {
            WebSocketFactory factory = new WebSocketFactory()
                    .setConnectionTimeout(CONNECTION_TIMEOUT_SECONDS * 1000);

            log.debug("📋 WebSocket factory configured with timeout: {}s", CONNECTION_TIMEOUT_SECONDS);

            twitchWebSocket = factory.createSocket(TWITCH_EVENTSUB_URL)
                    .addListener(this)
                    .setAutoFlush(true);

            log.debug("📨 Initiating async connection...");
            twitchWebSocket.connectAsynchronously();

            isConnecting = false;
            connectionStartTime = System.currentTimeMillis();
            log.info("✅ Twitch WebSocket connection initiated successfully - connection async in progress");

        } catch (Exception e) {
            log.error("❌ Failed to create/connect to Twitch WebSocket: {}", e.getMessage(), e);
            isConnecting = false;
            if (twitchWebSocket != null) {
                try {
                    twitchWebSocket.disconnect();
                    twitchWebSocket = null;
                } catch (Exception ex) {
                    log.debug("Error during disconnect on failed connect: {}", ex.getMessage());
                }
            }
            scheduleReconnect();
        }
    }

    /**
     * Close the WebSocket connection
     */
    public synchronized void disconnect() {
        shouldReconnect = false;
        if (twitchWebSocket != null) {
            log.info("🔌 Disconnecting from Twitch EventSub WebSocket...");
            try {
                twitchWebSocket.sendClose(1000, "Client shutdown");
                log.debug("✅ Close frame sent - waiting for response");
            } catch (Exception e) {
                log.debug("Error sending close frame: {}", e.getMessage());
            }
            try {
                twitchWebSocket.disconnect();
                log.info("✅ WebSocket disconnected successfully");
            } catch (Exception e) {
                log.warn("⚠️ Error during disconnect: {}", e.getMessage());
            }
            twitchWebSocket = null;
            sessionId = null;
        }
    }

    /**
     * Subscribe to Twitch EventSub subscriptions
     */
    public void subscribeToEvents() {
        if (sessionId == null) {
            log.warn("⚠️ Session ID not available yet, cannot subscribe to events");
            return;
        }

        log.info("📋 SUBSCRIBING TO EVENTS - Session ID: {}", sessionId);
        log.debug("   └─ Calling TwitchUtils.subscribeToTwitch()");
        TwitchUtils.subscribeToTwitch(sessionId);
        log.debug("   └─ Event subscriptions initiated");
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        log.info("✅ WEBSOCKET OPEN - Connected to Twitch EventSub WebSocket at: {}", TWITCH_EVENTSUB_URL);
        log.debug("📋 Connection headers: {}", headers);
        log.info("🔄 Awaiting session_welcome message from Twitch...");

        if (eventBroadcaster != null) {
            eventBroadcaster.broadcastConnectionStatus("connected");
            log.info("📡 Broadcasted connection status: connected");
        } else {
            log.warn("⚠️ Event broadcaster not available, cannot broadcast connection status");
        }

        // Schedule session welcome timeout check
        scheduleSessionWelcomeTimeout();
    }

    @Override
    public void onTextMessage(WebSocket websocket, String message) {
        try {
            log.info("🔵 RECEIVED TWITCH MESSAGE - Length: {} bytes", message.length());
            log.debug("📝 Full message content: {}", message);

            JsonNode messageNode = objectMapper.readTree(message);
            processMessage(messageNode);

        } catch (Exception e) {
            log.error("❌ ERROR processing Twitch text message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] payload) {
        try {
            String messageText = new String(payload);
            log.info("🔵 RECEIVED TWITCH BINARY MESSAGE - Length: {} bytes", payload.length);
            log.debug("📝 Binary message as text: {}", messageText);

            JsonNode messageNode = objectMapper.readTree(messageText);
            processMessage(messageNode);

        } catch (Exception e) {
            log.error("❌ ERROR processing Twitch binary message: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle errors in WebSocket connection
     */
    public void onError(WebSocket websocket, Throwable cause) {
        log.error("⚠️ WebSocket error: {}", cause.getMessage(), cause);
        if (eventBroadcaster != null) {
            eventBroadcaster.broadcastError("WebSocket error: " + cause.getMessage());
        }
        scheduleReconnect();
    }

    /**
     * Process incoming Twitch messages with detailed logging
     */
    private void processMessage(JsonNode message) {
        try {
            JsonNode metadata = message.get("metadata");
            if (metadata == null) {
                log.warn("❌ Message missing metadata: {}", message);
                return;
            }

            String messageType = metadata.get("message_type").asText();
            String messageId = metadata.get("message_id") != null ? metadata.get("message_id").asText() : "unknown";
            long timestamp = metadata.get("message_timestamp") != null ?
                            System.currentTimeMillis() : 0;

            log.info("📨 PROCESSING TWITCH MESSAGE - Type: {}, ID: {}, Timestamp: {}",
                    messageType, messageId, timestamp);

            // Log full message payload for debugging
            JsonNode payload = message.get("payload");
            if (payload != null) {
                log.debug("📄 MESSAGE PAYLOAD ({}): {}",
                         payload.toString().length() + " chars", payload.toString());
            }

            switch (messageType) {
                case "session_welcome":
                    log.info("🎉 SESSION WELCOME - Establishing Twitch connection");
                    handleSessionWelcome(message);
                    break;
                case "notification":
                    String subscriptionType = metadata.get("subscription_type").asText();
                    log.info("🔔 NOTIFICATION - Type: {} [ID: {}]", subscriptionType, messageId);
                    handleNotification(message);
                    break;
                case "session_keepalive":
                    log.debug("💓 KEEPALIVE - Twitch connection healthy [ID: {}]", messageId);
                    break;
                case "session_reconnect":
                    log.warn("🔄 RECONNECT REQUEST - Twitch requesting reconnection [ID: {}]", messageId);
                    handleReconnect(message);
                    break;
                case "revocation":
                    log.warn("🚫 SUBSCRIPTION REVOKED - [ID: {}] - {}", messageId, message);
                    break;
                default:
                    log.warn("❓ UNKNOWN MESSAGE TYPE: {} [ID: {}] - Full message: {}",
                            messageType, messageId, message);
            }
        } catch (Exception e) {
            log.error("❌ ERROR processing Twitch message: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle session welcome message with detailed logging
     */
    private void handleSessionWelcome(JsonNode message) {
        try {
            JsonNode payload = message.get("payload");
            JsonNode sessionNode = payload.get("session");
            this.sessionId = sessionNode.get("id").asText();

            long connectionDuration = System.currentTimeMillis() - connectionStartTime;
            log.info("🎯 Twitch session welcome received - Session ID: {} (connected in {} ms)",
                    sessionId, connectionDuration);

            // Log additional session information if available
            if (sessionNode.has("status")) {
                log.debug("   └─ Status: {}", sessionNode.get("status").asText());
            }
            if (sessionNode.has("reconnect_url")) {
                log.debug("   └─ Reconnect URL: {}", sessionNode.get("reconnect_url").asText());
            }
            if (sessionNode.has("created_at")) {
                log.debug("   └─ Created at: {}", sessionNode.get("created_at").asText());
            }

            if (eventBroadcaster != null) {
                eventBroadcaster.broadcastSessionWelcome(sessionId);
                log.debug("📡 Session welcome broadcasted to frontend");
            }

            // Subscribe to events after session is established
            subscribeToEvents();

        } catch (Exception e) {
            log.error("❌ Error handling session welcome: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle notification from Twitch with detailed logging
     */
    private void handleNotification(JsonNode message) {
        try {
            JsonNode metadata = message.get("metadata");
            String subscriptionType = metadata.get("subscription_type").asText();
            String subscriptionId = metadata.get("subscription_id") != null ?
                                    metadata.get("subscription_id").asText() : "unknown";
            JsonNode payload = message.get("payload");
            JsonNode event = payload.get("event");

            log.info("📊 NOTIFICATION EVENT - Type: {}, Subscription ID: {}", subscriptionType, subscriptionId);

            // Log event details based on type
            if (event != null) {
                long eventLength = event.toString().length();
                log.debug("   └─ Event payload size: {} bytes", eventLength);

                // Log specific event information for common event types
                if ("channel.chat.message".equals(subscriptionType)) {
                    if (event.has("chatter_user_id")) {
                        log.debug("   └─ Chatter ID: {}", event.get("chatter_user_id").asText());
                    }
                    if (event.has("message")) {
                        JsonNode msgNode = event.get("message");
                        if (msgNode.has("text")) {
                            String text = msgNode.get("text").asText();
                            if (text.length() > 100) {
                                log.debug("   └─ Message: {}... ({} chars)", text.substring(0, 100), text.length());
                            } else {
                                log.debug("   └─ Message: {}", text);
                            }
                        }
                    }
                } else if ("channel.follow".equals(subscriptionType)) {
                    if (event.has("user_login")) {
                        log.debug("   └─ Follower: {}", event.get("user_login").asText());
                    }
                } else if ("channel.subscribe".equals(subscriptionType)) {
                    if (event.has("user_login")) {
                        log.debug("   └─ Subscriber: {}", event.get("user_login").asText());
                    }
                } else if ("channel.raid".equals(subscriptionType)) {
                    if (event.has("from_broadcaster_user_login")) {
                        log.debug("   └─ Raider: {}", event.get("from_broadcaster_user_login").asText());
                    }
                } else if ("stream.online".equals(subscriptionType)) {
                    log.debug("   └─ Stream went online");
                } else if ("stream.offline".equals(subscriptionType)) {
                    log.debug("   └─ Stream went offline");
                }
            } else {
                log.warn("   └─ Event payload is null!");
            }

            // Route chat messages through the dedicated handler
            if ("channel.chat.message".equals(subscriptionType)) {
                log.debug("💬 CHAT MESSAGE - Processing through chat handler");
                if (chatMessageHandler != null) {
                    chatMessageHandler.processChatMessage(event);
                } else {
                    log.warn("Chat message handler not available");
                    if (eventBroadcaster != null) {
                        eventBroadcaster.broadcastTwitchEvent(subscriptionType, event);
                    }
                }
            } else {
                log.debug("📡 OTHER EVENT - Broadcasting to frontend");
                // Broadcast other event types normally
                if (eventBroadcaster != null) {
                    eventBroadcaster.broadcastTwitchEvent(subscriptionType, event);
                }
            }

        } catch (Exception e) {
            log.error("❌ ERROR handling notification: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle reconnect message from Twitch
     */
    private void handleReconnect(JsonNode message) {
        log.warn("Twitch requested reconnection");
        disconnect();
        scheduleReconnect();
    }

    /**
     * Schedule a reconnection attempt with exponential backoff and detailed logging
     */
    private void scheduleReconnect() {
        if (reconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
            log.error("❌ Max reconnection attempts ({}) reached - giving up", MAX_RECONNECT_ATTEMPTS);
            if (eventBroadcaster != null) {
                eventBroadcaster.broadcastError("Failed to reconnect after " + MAX_RECONNECT_ATTEMPTS + " attempts");
            }
            return;
        }

        long delayMs = RECONNECT_DELAY_MS * (long) Math.pow(2, reconnectAttempts);
        reconnectAttempts++;

        log.warn("🔄 RECONNECT SCHEDULED - Attempt: {}/{}, Delay: {} ms ({} seconds)",
                reconnectAttempts, MAX_RECONNECT_ATTEMPTS, delayMs, delayMs / 1000);

        new Thread(() -> {
            try {
                log.debug("   └─ Waiting {} ms before reconnection attempt {}...", delayMs, reconnectAttempts);
                Thread.sleep(delayMs);
                log.info("   └─ Executing reconnection attempt {}/{}", reconnectAttempts, MAX_RECONNECT_ATTEMPTS);
                connect();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Reconnect thread interrupted: {}", e.getMessage());
            }
        }, "TwitchWebSocketReconnect-" + reconnectAttempts).start();
    }

    /**
     * Get current session ID
     */
    public String getSessionId() {
        log.debug("Session ID requested - Current: {}", sessionId != null ? sessionId : "null");
        return sessionId;
    }

    /**
     * Check if connected
     */
    public boolean isConnected() {
        boolean connected = twitchWebSocket != null && twitchWebSocket.isOpen();
        log.debug("Connection status requested - Connected: {}", connected);
        return connected;
    }

    /**
     * Schedule a timeout check for session welcome with detailed logging
     */
    private void scheduleSessionWelcomeTimeout() {
        log.debug("📅 Scheduling session welcome timeout check - Timeout: {} ms", SESSION_WELCOME_TIMEOUT_MS);

        new Thread(() -> {
            try {
                Thread.sleep(SESSION_WELCOME_TIMEOUT_MS);
                if (twitchWebSocket != null && !twitchWebSocket.isOpen()) {
                    log.error("❌ WebSocket is not open after timeout");
                    scheduleReconnect();
                } else if (twitchWebSocket != null && sessionId == null) {
                    log.error("❌ Session welcome not received within {} ms - reconnecting...", SESSION_WELCOME_TIMEOUT_MS);
                    disconnect();
                    scheduleReconnect();
                } else if (sessionId != null) {
                    log.debug("✅ Session welcome received in time - Session ID: {}", sessionId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Session welcome timeout thread interrupted: {}", e.getMessage());
            }
        }, "TwitchSessionWelcomeTimeout").start();
    }
}
