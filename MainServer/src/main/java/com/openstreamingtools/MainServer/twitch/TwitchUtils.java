package com.openstreamingtools.MainServer.twitch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.api.OauthToken;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.utils.Utils;
import com.openstreamingtools.MainServer.websocket.WeboscketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TwitchUtils {
    public static final String TWITCH_API_GET_TOKEN_URL = "https://id.twitch.tv/oauth2/token";
    //  public static final String TWITCH_API_AUTHORIZE_URL = "https://id.twitch.tv/oauth2/authorize";
    public static final String TWITCH_EVENTSUB_WEBSOCKET_ADDRESS = "wss://eventsub.wss.twitch.tv/ws";
    public static final String TWITCC_GET_USER = "https://api.twitch.tv/helix/users";
    public static final String TWITCH_SUBSCRIBE = "https://api.twitch.tv/helix/eventsub/subscriptions";
    public static final String TWITCH_CHAT_MESSAGE = "https://api.twitch.tv/helix/chat/messages";

    public static void getAuthTokenFromTwitch(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", OSTConfiguration.TWITCH_CLIEND_ID);
        params.add("client_secret", OSTConfiguration.TWITCH_CLIENT_SECRET);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/");
        log.debug(code);
        OauthToken response = Utils.restClient.post()
                .uri(TWITCH_API_GET_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .body(OauthToken.class);
        log.debug(response.toString());
        OSTConfiguration.settings.setTwitchToken(response);
        OSTConfiguration.saveSettings();
    }

    public static void refreshAuthTokenFromTwitch(String token) throws UnsupportedEncodingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id",OSTConfiguration.TWITCH_CLIEND_ID);
        params.add("client_secret", OSTConfiguration.TWITCH_CLIENT_SECRET);
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", URLEncoder.encode(token, StandardCharsets.UTF_8));
        params.add("redirect_uri", "http://localhost:8080/");
        OauthToken response = null;
        try{
             response = Utils.restClient.post()
                    .uri(TWITCH_API_GET_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(params)
                    .retrieve()
                    .body(OauthToken.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (response != null){
            log.debug(response.toString());
            OSTConfiguration.settings.setTwitchToken(response);
            OSTConfiguration.saveSettings();
        }

    }

    public static void subscribeToTwitch(String sessionId) {
        String response = null;
        TwitchSubscribeMessage subscribeMessage = new TwitchSubscribeMessage(new TwitchSubscribeCondition(
                OSTConfiguration.settings.getTwitchUser().getId(), OSTConfiguration.settings.getTwitchUser().getId()),
                new TwitchSubscriptionTransport(sessionId));
            response = Utils.restClient.post()
                    .uri(TWITCH_SUBSCRIBE)
                    .header("Authorization","Bearer "
                            + OSTConfiguration.settings.getTwitchToken().getAccess_token())
                    .header("Client-Id", OSTConfiguration.TWITCH_CLIEND_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(subscribeMessage)
                    .retrieve()
                    .body(String.class);
        log.debug(response);
    }

    public static TwitchUsers getIdforUser(String name) throws JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("login", name);
        String response = Utils.restClient.get()
                .uri(TWITCC_GET_USER +"?login="+name)
                .header("Authorization","Bearer "
                        +OSTConfiguration.settings.getTwitchToken().getAccess_token())
                .header("Client-Id", OSTConfiguration.TWITCH_CLIEND_ID)
                .retrieve()
                .body(String.class);
        log.debug(response);

        return Utils.objectMapper.readValue(response , TwitchUsers.class ) ;
    }

    public static void sendToChat(String message){
        ChatMessage chatMessage = new ChatMessage("1109746665","1109746665",message);
        log.debug("Sending to Twitch chat: "+message);
        String respoonse = null;
        try {
            respoonse = Utils.restClient.post()
                    .uri(TWITCH_CHAT_MESSAGE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization","Bearer "
                            + OSTConfiguration.settings.getTwitchToken().getAccess_token())
                    .header("Client-Id", OSTConfiguration.TWITCH_CLIEND_ID)
                    .body(Utils.objectMapper.writeValueAsString(chatMessage))
                    .retrieve()
                    .body(String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.debug(respoonse);
    }
}
