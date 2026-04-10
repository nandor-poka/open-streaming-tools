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

import java.util.List;
import java.util.Map;

/**
 * Base class for Twitch WebSocket clients (Bot and Broadcaster)
 * Manages WebSocket connection lifecycle, session management, and event processing
 */
@Slf4j
public abstract class BaseTwitchWebSocketClient extends WebSocketAdapter {

    protected static final String TWITCH_EVENTSUB_URL = "wss://eventsub.wss.twitch.tv/ws";
    protected static final long HEARTBEAT_TIMEOUT_SECONDS = 10;
    protected static final long RECONNECT_DELAY_MS = 5000;
    protected static final int MAX_RECONNECT_ATTEMPTS = 10;
    protected static final long SESSION_WELCOME_TIMEOUT_MS = 30000;
    protected static final int CONNECTION_TIMEOUT_SECONDS = 30;

    protected WebSocket twitchWebSocket;
    protected String sessionId;
    protected ObjectMapper objectMapper;
    protected TwitchEventBroadcaster eventBroadcaster;
    protected TwitchChatMessageHandler chatMessageHandler;
    protected int reconnectAttempts = 0;
    protected boolean isConnecting = false;
    protected boolean shouldReconnect = true;
    protected long connectionStartTime = 0;
    protected final UserType clientType;

    public BaseTwitchWebSocketClient(UserType clientType) {
        this.clientType = clientType;
        this.objectMapper = Utils.objectMapper;
        log.debug("✅ {} WebSocket client initialized", clientType);
    }

    public void setEventBroadcaster(TwitchEventBroadcaster broadcaster) {
        this.eventBroadcaster = broadcaster;
        log.debug("📡 Event broadcaster set for {}", clientType);
    }

    public void setChatMessageHandler(TwitchChatMessageHandler handler) {
        this.chatMessageHandler = handler;
        log.debug("💬 Chat message handler set for {}", clientType);
    }

    /**
     * Check if this client should connect based on token availability
     */
    protected abstract boolean shouldConnect();

    /**
     * Subscribe to events specific to this client type
     */
    protected abstract void subscribeToEvents();

    /**
     * Establish connection to Twitch EventSub WebSocket
     */
    public synchronized void connect() {
        if (!shouldConnect()) {
            log.info("ℹ️ {} client not connecting - token not available or invalid", clientType);
            return;
        }

        if (isConnecting || twitchWebSocket != null) {
            log.debug("{} already connecting or connected", clientType);
            return;
        }

        isConnecting = true;
        log.info("🔌 {} establishing Twitch EventSub WebSocket connection to: {}", clientType, TWITCH_EVENTSUB_URL);

        try {
            WebSocketFactory factory = new WebSocketFactory()
                    .setConnectionTimeout(CONNECTION_TIMEOUT_SECONDS * 1000);

            log.debug("📋 {} WebSocket factory configured with timeout: {}s", clientType, CONNECTION_TIMEOUT_SECONDS);

            twitchWebSocket = factory.createSocket(TWITCH_EVENTSUB_URL)
                    .addListener(this)
                    .setAutoFlush(true);

            log.debug("📨 {} initiating async connection...", clientType);
            twitchWebSocket.connectAsynchronously();

            isConnecting = false;
            connectionStartTime = System.currentTimeMillis();
            log.info("✅ {} Twitch WebSocket connection initiated successfully", clientType);

        } catch (Exception e) {
            log.error("❌ {} failed to create/connect to Twitch WebSocket: {}", clientType, e.getMessage(), e);
            isConnecting = false;
            if (twitchWebSocket != null) {
                try {
                    twitchWebSocket.disconnect();
                    twitchWebSocket = null;
                } catch (Exception ex) {
                    log.debug("{} error during disconnect on failed connect: {}", clientType, ex.getMessage());
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
            log.info("🔌 {} disconnecting from Twitch EventSub WebSocket...", clientType);
            try {
                twitchWebSocket.sendClose(1000, clientType + " shutdown");
                log.debug("✅ {} close frame sent - waiting for response", clientType);
            } catch (Exception e) {
                log.debug("{} error sending close frame: {}", clientType, e.getMessage());
            }
            try {
                twitchWebSocket.disconnect();
                log.info("✅ {} WebSocket disconnected successfully", clientType);
            } catch (Exception e) {
                log.warn("⚠️ {} error during disconnect: {}", clientType, e.getMessage());
            }
            twitchWebSocket = null;
            sessionId = null;
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        log.info("✅ {} WEBSOCKET OPEN - Connected to Twitch EventSub WebSocket at: {}", clientType, TWITCH_EVENTSUB_URL);
        log.debug("📋 {} connection headers: {}", clientType, headers);
        log.info("🔄 {} awaiting session_welcome message from Twitch...", clientType);

        if (eventBroadcaster != null) {
            eventBroadcaster.broadcastConnectionStatus(clientType + "_connected");
            log.info("📡 {} broadcasted connection status: connected", clientType);
        }

        // Schedule session welcome timeout check
        scheduleSessionWelcomeTimeout();
    }

    @Override
    public void onTextMessage(WebSocket websocket, String message) {
        try {
            log.info("🔵 {} RECEIVED TWITCH MESSAGE - Length: {} bytes", clientType, message.length());
            log.debug("📝 {} full message content: {}", clientType, message);

            JsonNode messageNode = objectMapper.readTree(message);
            processMessage(messageNode);

        } catch (Exception e) {
            log.error("❌ {} ERROR processing Twitch text message: {}", clientType, e.getMessage(), e);
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] payload) {
        try {
            String messageText = new String(payload);
            log.info("🔵 {} RECEIVED TWITCH BINARY MESSAGE - Length: {} bytes", clientType, payload.length);
            log.debug("📝 {} binary message as text: {}", clientType, messageText);

            JsonNode messageNode = objectMapper.readTree(messageText);
            processMessage(messageNode);

        } catch (Exception e) {
            log.error("❌ {} ERROR processing Twitch binary message: {}", clientType, e.getMessage(), e);
        }
    }

    /**
     * Handle errors in WebSocket connection
     */
    public void onError(WebSocket websocket, Throwable cause) {
        log.error("⚠️ {} WebSocket error: {}", clientType, cause.getMessage(), cause);
        if (eventBroadcaster != null) {
            eventBroadcaster.broadcastError(clientType + " WebSocket error: " + cause.getMessage());
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
                log.warn("❌ {} message missing metadata: {}", clientType, message);
                return;
            }

            String messageType = metadata.get("message_type").asText();
            String messageId = metadata.get("message_id") != null ? metadata.get("message_id").asText() : "unknown";
            long timestamp = metadata.get("message_timestamp") != null ?
                            System.currentTimeMillis() : 0;

            log.info("📨 {} PROCESSING TWITCH MESSAGE - Type: {}, ID: {}, Timestamp: {}",
                    clientType, messageType, messageId, timestamp);

            // Log full message payload for debugging
            JsonNode payload = message.get("payload");
            if (payload != null) {
                log.debug("📄 {} MESSAGE PAYLOAD ({}): {}",
                         clientType, payload.toString().length() + " chars", payload.toString());
            }

            switch (messageType) {
                case "session_welcome":
                    log.info("🎉 {} SESSION WELCOME - Establishing Twitch connection", clientType);
                    handleSessionWelcome(message);
                    break;
                case "notification":
                    String subscriptionType = metadata.get("subscription_type").asText();
                    log.info("🔔 {} NOTIFICATION - Type: {} [ID: {}]", clientType, subscriptionType, messageId);
                    handleNotification(message);
                    break;
                case "session_keepalive":
                    log.debug("💓 {} KEEPALIVE - Twitch connection healthy [ID: {}]", clientType, messageId);
                    break;
                case "session_reconnect":
                    log.warn("🔄 {} RECONNECT REQUEST - Twitch requesting reconnection [ID: {}]", clientType, messageId);
                    handleReconnect(message);
                    break;
                case "revocation":
                    log.warn("🚫 {} SUBSCRIPTION REVOKED - [ID: {}] - {}", clientType, messageId, message);
                    break;
                default:
                    log.warn("❓ {} UNKNOWN MESSAGE TYPE: {} [ID: {}] - Full message: {}",
                            clientType, messageType, messageId, message);
            }
        } catch (Exception e) {
            log.error("❌ {} ERROR processing Twitch message: {}", clientType, e.getMessage(), e);
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
            log.info("🎯 {} Twitch session welcome received - Session ID: {} (connected in {} ms)",
                    clientType, sessionId, connectionDuration);

            // Log additional session information if available
            if (sessionNode.has("status")) {
                log.debug("   └─ {} Status: {}", clientType, sessionNode.get("status").asText());
            }
            if (sessionNode.has("reconnect_url")) {
                log.debug("   └─ {} Reconnect URL: {}", clientType, sessionNode.get("reconnect_url").asText());
            }
            if (sessionNode.has("created_at")) {
                log.debug("   └─ {} Created at: {}", clientType, sessionNode.get("created_at").asText());
            }

            if (eventBroadcaster != null) {
                eventBroadcaster.broadcastSessionWelcome(clientType + "_" + sessionId);
                log.debug("📡 {} session welcome broadcasted to frontend", clientType);
            }

            // Subscribe to events after session is established
            subscribeToEvents();

        } catch (Exception e) {
            log.error("❌ {} error handling session welcome: {}", clientType, e.getMessage(), e);
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

            log.info("📊 {} NOTIFICATION EVENT - Type: {}, Subscription ID: {}", clientType, subscriptionType, subscriptionId);

            // Log event details based on type
            if (event != null) {
                long eventLength = event.toString().length();
                log.debug("   └─ {} event payload size: {} bytes", clientType, eventLength);

                // Log specific event information for common event types
                if ("channel.chat.message".equals(subscriptionType)) {
                    if (event.has("chatter_user_id")) {
                        log.debug("   └─ {} Chatter ID: {}", clientType, event.get("chatter_user_id").asText());
                    }
                    if (event.has("message")) {
                        JsonNode msgNode = event.get("message");
                        if (msgNode.has("text")) {
                            String text = msgNode.get("text").asText();
                            if (text.length() > 100) {
                                log.debug("   └─ {} Message: {}... ({} chars)", clientType, text.substring(0, 100), text.length());
                            } else {
                                log.debug("   └─ {} Message: {}", clientType, text);
                            }
                        }
                    }
                } else if ("channel.follow".equals(subscriptionType)) {
                    if (event.has("user_login")) {
                        log.debug("   └─ {} Follower: {}", clientType, event.get("user_login").asText());
                    }
                } else if ("channel.subscribe".equals(subscriptionType)) {
                    if (event.has("user_login")) {
                        log.debug("   └─ {} Subscriber: {}", clientType, event.get("user_login").asText());
                    }
                } else if ("channel.raid".equals(subscriptionType)) {
                    if (event.has("from_broadcaster_user_login")) {
                        log.debug("   └─ {} Raider: {}", clientType, event.get("from_broadcaster_user_login").asText());
                    }
                } else if ("stream.online".equals(subscriptionType)) {
                    log.debug("   └─ {} Stream went online", clientType);
                } else if ("stream.offline".equals(subscriptionType)) {
                    log.debug("   └─ {} Stream went offline", clientType);
                }
            } else {
                log.warn("   └─ {} event payload is null!", clientType);
            }

            // Route chat messages through the dedicated handler
            if ("channel.chat.message".equals(subscriptionType)) {
                log.debug("💬 {} CHAT MESSAGE - Processing through chat handler", clientType);
                if (chatMessageHandler != null) {
                    chatMessageHandler.processChatMessage(event);
                } else {
                    log.warn("{} chat message handler not available", clientType);
                    if (eventBroadcaster != null) {
                        eventBroadcaster.broadcastTwitchEvent(subscriptionType, event);
                    }
                }
            } else {
                log.debug("📡 {} OTHER EVENT - Broadcasting to frontend", clientType);
                // Broadcast other event types normally
                if (eventBroadcaster != null) {
                    eventBroadcaster.broadcastTwitchEvent(subscriptionType, event);
                }
            }

        } catch (Exception e) {
            log.error("❌ {} ERROR handling notification: {}", clientType, e.getMessage(), e);
        }
    }

    /**
     * Handle reconnect message from Twitch
     */
    private void handleReconnect(JsonNode message) {
        log.warn("{} Twitch requested reconnection", clientType);
        disconnect();
        scheduleReconnect();
    }

    /**
     * Schedule a reconnection attempt with exponential backoff and detailed logging
     */
    private void scheduleReconnect() {
        if (reconnectAttempts >= MAX_RECONNECT_ATTEMPTS) {
            log.error("❌ {} max reconnection attempts ({}) reached - giving up", clientType, MAX_RECONNECT_ATTEMPTS);
            if (eventBroadcaster != null) {
                eventBroadcaster.broadcastError(clientType + " failed to reconnect after " + MAX_RECONNECT_ATTEMPTS + " attempts");
            }
            return;
        }

        long delayMs = RECONNECT_DELAY_MS * (long) Math.pow(2, reconnectAttempts);
        reconnectAttempts++;

        log.warn("🔄 {} RECONNECT SCHEDULED - Attempt: {}/{}, Delay: {} ms ({} seconds)",
                clientType, reconnectAttempts, MAX_RECONNECT_ATTEMPTS, delayMs, delayMs / 1000);

        new Thread(() -> {
            try {
                log.debug("   └─ {} waiting {} ms before reconnection attempt {}...", clientType, delayMs, reconnectAttempts);
                Thread.sleep(delayMs);
                log.info("   └─ {} executing reconnection attempt {}/{}", clientType, reconnectAttempts, MAX_RECONNECT_ATTEMPTS);
                connect();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} reconnect thread interrupted: {}", clientType, e.getMessage());
            }
        }, "TwitchWebSocketReconnect-" + clientType + "-" + reconnectAttempts).start();
    }

    /**
     * Get current session ID
     */
    public String getSessionId() {
        log.debug("{} session ID requested - Current: {}", clientType, sessionId != null ? sessionId : "null");
        return sessionId;
    }

    /**
     * Check if connected
     */
    public boolean isConnected() {
        boolean connected = twitchWebSocket != null && twitchWebSocket.isOpen();
        log.debug("{} connection status requested - Connected: {}", clientType, connected);
        return connected;
    }

    /**
     * Schedule a timeout check for session welcome with detailed logging
     */
    private void scheduleSessionWelcomeTimeout() {
        log.debug("📅 {} scheduling session welcome timeout check - Timeout: {} ms", clientType, SESSION_WELCOME_TIMEOUT_MS);

        new Thread(() -> {
            try {
                Thread.sleep(SESSION_WELCOME_TIMEOUT_MS);
                if (twitchWebSocket != null && !twitchWebSocket.isOpen()) {
                    log.error("❌ {} WebSocket is not open after timeout", clientType);
                    scheduleReconnect();
                } else if (twitchWebSocket != null && sessionId == null) {
                    log.error("❌ {} session welcome not received within {} ms - reconnecting...", clientType, SESSION_WELCOME_TIMEOUT_MS);
                    disconnect();
                    scheduleReconnect();
                } else if (sessionId != null) {
                    log.debug("✅ {} session welcome received in time - Session ID: {}", clientType, sessionId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} session welcome timeout thread interrupted: {}", clientType, e.getMessage());
            }
        }, "TwitchSessionWelcomeTimeout-" + clientType).start();
    }
}
