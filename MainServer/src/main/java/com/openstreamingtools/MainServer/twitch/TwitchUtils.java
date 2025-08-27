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
import java.util.Arrays;

@Slf4j
public class TwitchUtils {
    public static final String TWITCH_API_GET_TOKEN_URL = "https://id.twitch.tv/oauth2/token";
    //public static final String TWITCH_API_AUTHORIZE_URL = "https://id.twitch.tv/oauth2/authorize";
    public static final String TWITCH_EVENTSUB_WEBSOCKET_ADDRESS = "wss://eventsub.wss.twitch.tv/ws";
    public static final String TWITCC_GET_USER = "https://api.twitch.tv/helix/users";
    public static final String TWITCH_SUBSCRIBE = "https://api.twitch.tv/helix/eventsub/subscriptions";
    public static final String TWITCH_CHAT_MESSAGE = "https://api.twitch.tv/helix/chat/messages";
    public static final String TWITCH_VALIDATE_TOKEN = "https://id.twitch.tv/oauth2/validate";

    public static void getAuthTokenFromTwitch(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", OSTConfiguration.TWITCH_CLIEND_ID);
        params.add("client_secret", OSTConfiguration.TWITCH_CLIENT_SECRET);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/");

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
        OSTConfiguration.settings.setTwitchToken(response);
        OSTConfiguration.saveSettings();
    }

    public static void refreshAuthTokenFromTwitch() throws UnsupportedEncodingException {
        if (!validateToken()){
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id",OSTConfiguration.TWITCH_CLIEND_ID);
            params.add("client_secret", OSTConfiguration.TWITCH_CLIENT_SECRET);
            params.add("grant_type", "refresh_token");
            params.add("refresh_token", URLEncoder.encode(OSTConfiguration.settings.getTwitchToken().getRefresh_token(), StandardCharsets.UTF_8));
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
                OSTConfiguration.settings.setTwitchToken(response);
                OSTConfiguration.settings.setTwitchStatus(true);
                OSTConfiguration.saveSettings();
            }
        }


    }

    public static boolean validateToken(){
        ResponseEntity response = Utils.restClient.get()
                .uri(TWITCH_VALIDATE_TOKEN)
                .header("Authorization","Bearer "
                        + OSTConfiguration.settings.getTwitchToken().getAccess_token())
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
        return false;
    }
    public static String subscribeToTwitch(String sessionId) {
        String response = null;
        if (OSTConfiguration.settings.getTwitchToken() == null){
            log.debug("Twitch user in settings is empty. Login first.");
            return "Twitch user in settings is empty. Login first.";
        }
        TwitchSubscribeMessage subscribeMessage = new TwitchSubscribeMessage(new TwitchSubscribeCondition(
                OSTConfiguration.settings.getTwitchUser().getId(), OSTConfiguration.settings.getBotUser().getId()),
                new TwitchSubscriptionTransport(sessionId));
            response = Utils.restClient.post()
                    .uri(TWITCH_SUBSCRIBE)
                    .header("Authorization","Bearer "
                            + OSTConfiguration.settings.getTwitchToken().getAccess_token())
                    .header("Client-Id", OSTConfiguration.TWITCH_CLIEND_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(subscribeMessage)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError
                            , (request, resp) -> {
                        log.error(resp.getStatusText());
                    })
                    .body(String.class);
        log.debug(response);
        return "logged in.";
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
                .onStatus(HttpStatusCode::is4xxClientError
                        , (request, resp) -> {
                            log.error(resp.getStatusText());
                        })
                .body(String.class);
        log.debug(response);

        return Utils.objectMapper.readValue(response , TwitchUsers.class ) ;
    }

    public static void sendToChat(String message){
        ChatMessage chatMessage = new ChatMessage(OSTConfiguration.settings.getTwitchUser().getId()
                ,OSTConfiguration.settings.getBotUser().getId(),message);
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
}
