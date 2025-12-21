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

        OauthToken response = Utils.restClient.post()
                .uri(TWITCH_API_GET_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError
                        , (request, resp) -> {
                            log.error(resp.toString());
                        })
                .body(OauthToken.class);
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

    public static void refreshAuthTokenFromTwitch() throws UnsupportedEncodingException {
        if (!validateToken()){
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id",OSTConfiguration.getTWITCH_CLIEND_ID());
            params.add("client_secret", OSTConfiguration.getTWITCH_CLIENT_SECRET());
            params.add("grant_type", "refresh_token");
            params.add("refresh_token", URLEncoder.encode(OSTConfiguration.settings.getTwitchBroadcasterToken().getRefresh_token(), StandardCharsets.UTF_8));
            params.add("redirect_uri", "http://localhost:8080/");
            OauthToken response = null;
            try{
                response = Utils.restClient.post()
                        .uri(TWITCH_API_GET_TOKEN_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(params)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError
                                , (request, resp) -> {
                                    log.error(resp.getStatusText());
                                })
                        .body(OauthToken.class);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            if (response != null){
                log.debug(response.toString());
                OSTConfiguration.settings.setTwitchBotToken(response);
                OSTConfiguration.settings.setTwitchStatus(true);
                OSTConfiguration.saveSettings();
            }
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
            log.debug("Twitch user in settings is empty. Login first.");
            return "Twitch user in settings is empty. Login first.";
        }
        TwitchSubscriptionTransport transport = new TwitchSubscriptionTransport(sessionId);
        String tokenString = OSTConfiguration.settings.getTwitchBroadcasterToken().getAccess_token();
        for(String subType : subscriptions){
                    TwitchSubscribeMessage subscribeMessage = new TwitchSubscribeMessage();
                    subscribeMessage.setType(subType);
                    subscribeMessage.setTransport(transport);
                    TwitchSubscribtionCondition condition = new TwitchSubscribtionCondition();
                    switch (subType){
                        case "channel.chat.message":
                            condition = new TwitchChatMessageSubscribeCondition(
                                    OSTConfiguration.settings.getTwitchUser().getId());
                            break;
                        case "channel.channel_points_custom_reward_redemption.add":
                        case "channel.channel_points_automatic_reward_redemption.add":
                           break;
                    }
                    subscribeMessage.setCondition(condition);
            response = Utils.restClient.post()
                    .uri(TWITCH_SUBSCRIBE)
                    .header("Authorization","Bearer "
                            + tokenString)
                    .header("Client-Id", OSTConfiguration.getTWITCH_CLIEND_ID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(subscribeMessage)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError
                            , (request, resp) -> {
                                log.error(resp.getStatusText());
                            })
                    .body(String.class);
            log.debug(subscribeMessage.toString(), response);
        }

        if (tokenValidationTask == null){
            tokenValidationTask = new TokenValidationTask();
            Utils.timer.scheduleAtFixedRate(tokenValidationTask, 0, Utils.HOUR_IN_MILLIS);
        }
        return "logged in as "+OSTConfiguration.settings.getBotUser().getLogin();
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
