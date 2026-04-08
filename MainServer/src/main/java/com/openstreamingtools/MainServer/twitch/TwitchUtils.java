package com.openstreamingtools.MainServer.twitch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.api.OauthToken;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Slf4j
public class TwitchUtils {
    public static final String TWITCH_API_GET_TOKEN_URL = "https://id.twitch.tv/oauth2/token";
    //public static final String TWITCH_API_AUTHORIZE_URL = "https://id.twitch.tv/oauth2/authorize";
    //public static final String TWITCH_EVENTSUB_WEBSOCKET_ADDRESS = "wss://eventsub.wss.twitch.tv/ws";
    public static final String TWITCH_GET_USER = "https://api.twitch.tv/helix/users";
    public static final String TWITCH_SUBSCRIBE = "https://api.twitch.tv/helix/eventsub/subscriptions";
    public static final String TWITCH_CHAT_MESSAGE = "https://api.twitch.tv/helix/chat/messages";
    public static final String TWITCH_VALIDATE_TOKEN = "https://id.twitch.tv/oauth2/validate";
    public static final String SHOUTOUT_COMMAND = "/shoutout ";
    private static TokenValidationTask tokenValidationTask = null;
    private static final String[] subscriptions= new String[]{
            "channel.chat.message",
            "channel.channel_points_custom_reward_redemption.add",
            "channel.channel_points_automatic_reward_redemption.add"
    };

    public enum TwitchUserType {
        BOT,
        BROADCASTER
    }

    public static void getAuthTokenFromTwitch(String code, TwitchUserType userType){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", OSTConfiguration.getTWITCH_CLIEND_ID());
        params.add("client_secret", OSTConfiguration.getTWITCH_CLIENT_SECRET());
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        switch (userType){
            case BOT:
                params.add("redirect_uri", "http://localhost:8080/api/twitchBot");
                break;
            case BROADCASTER:
                params.add("redirect_uri", "http://localhost:8080/api/twitchBroadcaster");
                break;
        }

        // Log detailed request information
        log.info("🔗 POST {} - Getting auth token for {}", TWITCH_API_GET_TOKEN_URL, userType);
        log.debug("📋 Request params: client_id={}, grant_type=authorization_code, redirect_uri={}",
                 OSTConfiguration.getTWITCH_CLIEND_ID(),
                 userType == TwitchUserType.BOT ? "http://localhost:8080/api/twitchBot" : "http://localhost:8080/api/twitchBroadcaster");

        OauthToken response = Utils.restClient.post()
                .uri(TWITCH_API_GET_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError
                        , (request, resp) -> {
                            log.error("❌ POST {} failed: HTTP {} - {}", TWITCH_API_GET_TOKEN_URL, resp.getStatusCode(), resp.getStatusText());
                        })
                .body(OauthToken.class);

        log.info("✅ POST {} successful - Auth token retrieved for {}", TWITCH_API_GET_TOKEN_URL, userType);
        log.debug("📄 Response: access_token length={}, token_type={}, expires_in={}",
                 response.getAccess_token() != null ? response.getAccess_token().length() : 0,
                 response.getToken_type(),
                 response.getExpires_in());
        log.debug(response.toString());
        switch (userType){
            case BOT:
                OSTConfiguration.settings.setTwitchBotToken(response);
                break;
            case BROADCASTER:
               OSTConfiguration.settings.setTwitchBroadcasterToken(response);
        }
        OSTConfiguration.saveSettings();
    }

    public static boolean refreshBotToken() {
        if (OSTConfiguration.settings.getTwitchBotToken() == null ||
            OSTConfiguration.settings.getTwitchBotToken().getRefresh_token() == null) {
            log.warn("⚠️ Bot token or refresh token is null, cannot refresh");
            OSTConfiguration.settings.setBotTokenRefreshSuccess(false);
            return false;
        }

        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", OSTConfiguration.getTWITCH_CLIEND_ID());
            params.add("client_secret", OSTConfiguration.getTWITCH_CLIENT_SECRET());
            params.add("grant_type", "refresh_token");
            params.add("refresh_token", URLEncoder.encode(OSTConfiguration.settings.getTwitchBotToken().getRefresh_token(), StandardCharsets.UTF_8));
            params.add("redirect_uri", "http://localhost:8080/api/twitchBot");

            log.info("🔄 POST {} - Refreshing bot auth token", TWITCH_API_GET_TOKEN_URL);

            OauthToken response = Utils.restClient.post()
                    .uri(TWITCH_API_GET_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(params)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, resp) -> {
                        log.error("❌ POST {} bot refresh failed: HTTP {} - {}", TWITCH_API_GET_TOKEN_URL, resp.getStatusCode(), resp.getStatusText());
                    })
                    .body(OauthToken.class);

            log.info("✅ POST {} bot refresh successful - Token refreshed", TWITCH_API_GET_TOKEN_URL);
            OSTConfiguration.settings.setTwitchBotToken(response);
            OSTConfiguration.settings.setBotTokenRefreshSuccess(true);
            OSTConfiguration.saveSettings();
            return true;

        } catch (Exception e) {
            log.error("❌ POST {} bot refresh exception: {}", TWITCH_API_GET_TOKEN_URL, e.getMessage());
            OSTConfiguration.settings.setBotTokenRefreshSuccess(false);
            return false;
        }
    }

    public static boolean refreshBroadcasterToken() {
        if (OSTConfiguration.settings.getTwitchBroadcasterToken() == null ||
            OSTConfiguration.settings.getTwitchBroadcasterToken().getRefresh_token() == null) {
            log.warn("⚠️ Broadcaster token or refresh token is null, cannot refresh");
            OSTConfiguration.settings.setBroadcasterTokenRefreshSuccess(false);
            return false;
        }

        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", OSTConfiguration.getTWITCH_CLIEND_ID());
            params.add("client_secret", OSTConfiguration.getTWITCH_CLIENT_SECRET());
            params.add("grant_type", "refresh_token");
            params.add("refresh_token", URLEncoder.encode(OSTConfiguration.settings.getTwitchBroadcasterToken().getRefresh_token(), StandardCharsets.UTF_8));
            params.add("redirect_uri", "http://localhost:8080/api/twitchBroadcaster");

            log.info("🔄 POST {} - Refreshing broadcaster auth token", TWITCH_API_GET_TOKEN_URL);

            OauthToken response = Utils.restClient.post()
                    .uri(TWITCH_API_GET_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(params)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, resp) -> {
                        log.error("❌ POST {} broadcaster refresh failed: HTTP {} - {}", TWITCH_API_GET_TOKEN_URL, resp.getStatusCode(), resp.getStatusText());
                    })
                    .body(OauthToken.class);

            log.info("✅ POST {} broadcaster refresh successful - Token refreshed", TWITCH_API_GET_TOKEN_URL);
            OSTConfiguration.settings.setTwitchBroadcasterToken(response);
            OSTConfiguration.settings.setBroadcasterTokenRefreshSuccess(true);
            OSTConfiguration.saveSettings();
            return true;

        } catch (Exception e) {
            log.error("❌ POST {} broadcaster refresh exception: {}", TWITCH_API_GET_TOKEN_URL, e.getMessage());
            OSTConfiguration.settings.setBroadcasterTokenRefreshSuccess(false);
            return false;
        }
    }

    public static boolean validateToken(){
        try {
            ResponseEntity response = Utils.restClient.get()
                    .uri(TWITCH_VALIDATE_TOKEN)
                    .header("Authorization","Bearer "
                            + OSTConfiguration.settings.getTwitchBroadcasterToken().getAccess_token())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError
                            , (request, resp) -> {
                                log.error(resp.getStatusText());
                            })
                    .toBodilessEntity();
            log.debug(response.toString());
            if (response.getStatusCode().is2xxSuccessful()){
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return false;
    }
    public static String subscribeToTwitch(String sessionId) {
        String response = null;
        if (OSTConfiguration.settings.getTwitchBroadcasterToken() == null){
            log.debug("Twitch broadcaster token is empty. Login first.");
            return "Twitch broadcaster token is empty. Login first.";
        }
        if (OSTConfiguration.settings.getTwitchBotToken() == null){
            log.debug("Twitch bot token is empty. Login first.");
            return "Twitch bot token is empty. Login first.";
        }

        TwitchSubscriptionTransport transport = new TwitchSubscriptionTransport(sessionId);

        // Subscribe to chat messages for bot's channel
        if (OSTConfiguration.settings.getBotUser() != null) {
            log.info("📝 Subscribing to chat messages for bot channel: {}", OSTConfiguration.settings.getBotUser().getLogin());
            TwitchSubscribeMessage chatSubscribeMessage = new TwitchSubscribeMessage();
            chatSubscribeMessage.setType("channel.chat.message");
            chatSubscribeMessage.setTransport(transport);
            TwitchSubscribtionCondition chatCondition = new TwitchChatMessageSubscribeCondition(
                OSTConfiguration.settings.getBotUser().getId());
            chatSubscribeMessage.setCondition(chatCondition);

            String chatToken = OSTConfiguration.settings.getTwitchBotToken().getAccess_token();
            log.debug("🔑 Using bot token for chat subscription (length: {})", chatToken.length());

            // Log detailed payload for chat subscription
            try {
                String chatPayloadJson = Utils.objectMapper.writeValueAsString(chatSubscribeMessage);
                log.info("📤 CHAT SUBSCRIPTION PAYLOAD - Length: {} bytes", chatPayloadJson.length());
                log.debug("📄 Chat subscription payload: {}", chatPayloadJson);
                log.debug("   └─ Type: {}", chatSubscribeMessage.getType());
                log.debug("   └─ Transport: Session ID {}", transport.getSession_id());
                log.debug("   └─ Condition: Broadcaster ID {}", chatCondition.getBroadcaster_user_id());
            } catch (JsonProcessingException e) {
                log.warn("⚠️ Could not serialize chat subscription payload for logging: {}", e.getMessage());
            }

            response = Utils.restClient.post()
                .uri(TWITCH_SUBSCRIBE)
                .header("Authorization","Bearer " + chatToken)
                .header("Client-Id", OSTConfiguration.getTWITCH_CLIEND_ID())
                .contentType(MediaType.APPLICATION_JSON)
                .body(chatSubscribeMessage)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, resp) -> {
                    log.error("❌ Failed to subscribe to chat messages: HTTP {} - {}", resp.getStatusCode(), resp.getStatusText());
                })
                .body(String.class);
            log.info("✅ Chat message subscription response: {}", response);
        } else {
            log.warn("⚠️ Bot user not configured, skipping chat message subscription");
        }

        // Subscribe to channel points for broadcaster's channel
        if (OSTConfiguration.settings.getTwitchUser() != null) {
            log.info("🎯 Subscribing to channel points for broadcaster channel: {}", OSTConfiguration.settings.getTwitchUser().getLogin());

            String[] channelSubscriptions = {
                "channel.channel_points_custom_reward_redemption.add",
                "channel.channel_points_automatic_reward_redemption.add"
            };

            String broadcasterToken = OSTConfiguration.settings.getTwitchBroadcasterToken().getAccess_token();
            log.debug("🔑 Using broadcaster token for channel subscriptions (length: {})", broadcasterToken.length());
            TwitchSubscribtionCondition channelCondition = new TwitchChatMessageSubscribeCondition(
                    OSTConfiguration.settings.getBotUser().getId());
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
                    log.debug("   └─ Condition: None (broadcaster channel default)");
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
        } else {
            log.warn("⚠️ Broadcaster user not configured, skipping channel subscriptions");
        }

        if (tokenValidationTask == null){
            tokenValidationTask = new TokenValidationTask();
            Utils.timer.scheduleAtFixedRate(tokenValidationTask, 0, Utils.HOUR_IN_MILLIS);
        }

        String botChannel = OSTConfiguration.settings.getBotUser() != null ?
            OSTConfiguration.settings.getBotUser().getLogin() : "unknown";
        String broadcasterChannel = OSTConfiguration.settings.getTwitchUser() != null ?
            OSTConfiguration.settings.getTwitchUser().getLogin() : "unknown";

        return "Subscribed - Chat: " + botChannel + ", Channel Points: " + broadcasterChannel;
    }

    public static TwitchUsers getIdforUser(String name) throws JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("login", name);
        String response = Utils.restClient.get()
                .uri(TWITCH_GET_USER +"?login="+name)
                .header("Authorization","Bearer "
                        +OSTConfiguration.settings.getTwitchBroadcasterToken().getAccess_token())
                .header("Client-Id", OSTConfiguration.getTWITCH_CLIEND_ID())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError
                        , (request, resp) -> {
                            log.error(resp.getStatusText());
                        })
                .body(String.class);
        log.debug(response);

        return Utils.objectMapper.readValue(response , TwitchUsers.class ) ;
    }

    public static void sendToChat(String message){
        if (OSTConfiguration.settings.getTwitchUser() == null
        || OSTConfiguration.settings.getBotUser() == null){
            return;
        }
        
        ChatMessage chatMessage = new ChatMessage(OSTConfiguration.settings.getTwitchUser().getId()
                ,OSTConfiguration.settings.getBotUser().getId(),message);

        // Log detailed payload for chat message
        try {
            String chatMessageJson = Utils.objectMapper.writeValueAsString(chatMessage);
            log.info("💬 CHAT MESSAGE PAYLOAD - Length: {} bytes", chatMessageJson.length());
            log.debug("📄 Chat message payload: {}", chatMessageJson);
            log.debug("   └─ Broadcaster ID: {}", chatMessage.getBroadcaster_id());
            log.debug("   └─ Sender ID: {}", chatMessage.getSender_id());
            log.debug("   └─ Message: {}", chatMessage.getMessage());
        } catch (JsonProcessingException e) {
            log.warn("⚠️ Could not serialize chat message payload for logging: {}", e.getMessage());
        }

        log.debug("Sending to Twitch chat: "+message);
        String respoonse = null;
        try {
            respoonse = Utils.restClient.post()
                    .uri(TWITCH_CHAT_MESSAGE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization","Bearer "
                            + OSTConfiguration.settings.getTwitchBotToken().getAccess_token())
                    .header("Client-Id", OSTConfiguration.getTWITCH_CLIEND_ID())
                    .body(Utils.objectMapper.writeValueAsString(chatMessage))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError
                            , (request, resp) -> {
                                log.error(resp.getStatusText());
                            })
                    .body(String.class);

            // Log detailed response information
            log.info("✅ Message sent to chat - Response: {}", respoonse);
            log.debug("📄 Response details: {}", respoonse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.debug(respoonse);
    }

    public static String getSubscriptions() {
        String response = Utils.restClient.get()
                .uri(TWITCH_SUBSCRIBE)
                .header("Authorization","Bearer "
                        + OSTConfiguration.settings.getTwitchBroadcasterToken().getAccess_token())
                .header("Client-Id", OSTConfiguration.getTWITCH_CLIEND_ID())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError
                        , (request, resp) -> {
                            log.error(resp.getStatusText());
                        })
                .body(String.class);
        log.debug(response);
        return response;
    }
}
