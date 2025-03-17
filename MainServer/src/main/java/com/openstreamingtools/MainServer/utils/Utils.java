package com.openstreamingtools.MainServer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.api.OauthToken;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.twitch.TwitchUser;
import com.openstreamingtools.MainServer.twitch.TwitchUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.UUID;

@Slf4j
public class Utils {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Timer timer = new Timer();
    public static final String TWITCH_API_GET_TOKEN_URL = "https://id.twitch.tv/oauth2/token";
  //  public static final String TWITCH_API_AUTHORIZE_URL = "https://id.twitch.tv/oauth2/authorize";
    public static final String TWITCH_EVENTSUB_WEBSOCKET_ADDRESS = "wss://eventsub.wss.twitch.tv/ws";
    public static final String TWITCC_GET_USER = "https://api.twitch.tv/helix/users";
    public static final String TWITCH_SUBSCRIBE = "https://api.twitch.tv/helix/eventsub/subscriptions";
    public static final String TWITCH_CHAT_MESSAGE = "https://api.twitch.tv/helix/chat/messages";
    public static RestClient restClient = RestClient.create();

    public static void getAuthTokenFromTwitch(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id",OSTConfiguration.TWITCH_CLIEND_ID);
        params.add("client_secret", OSTConfiguration.TWITCH_CLIENT_SECRET);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/");
        log.debug(code);
        OauthToken response = restClient.post()
                .uri(TWITCH_API_GET_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, resp) -> {
                            log.error("Error getting token from Twitch token: " + resp.getStatusCode());
                            log.error(resp.getStatusText());
                        })
                .body(OauthToken.class);
        if (response != null){
            log.debug(response.toString());
            OSTConfiguration.settings.setTwitchToken(response);
            OSTConfiguration.saveSettings();
        }

    }

    public static void refreshAuthTokenFromTwitch(String token) throws UnsupportedEncodingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id",OSTConfiguration.TWITCH_CLIEND_ID);
        params.add("client_secret", OSTConfiguration.TWITCH_CLIENT_SECRET);
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", URLEncoder.encode(token, StandardCharsets.UTF_8));
        params.add("redirect_uri", "http://localhost:8080/");

        try{
            OauthToken response = restClient.post()
                    .uri(TWITCH_API_GET_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(params)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            (request, resp) -> {
                                log.error("Error refreshing token: " + resp.getStatusCode());
                                log.error(resp.getStatusText());
                            })
                    .body(OauthToken.class);
            if (response != null) {
                log.debug(response.toString());
                OSTConfiguration.settings.setTwitchToken(response);
                OSTConfiguration.saveSettings();

            }
        } catch (Exception e) {
          log.error(e.getMessage());
        }


    }

    public static void subscribeToTwitch() {
        String response = null;
        try {
            response = restClient.post()
                    .uri(TWITCH_SUBSCRIBE)
                    .header("Authorization","Bearer "
                            + OSTConfiguration.settings.getTwitchToken().getAccess_token())
                    .header("Client-Id", OSTConfiguration.TWITCH_CLIEND_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Utils.objectMapper.readValue(
                            """
        {
        "type": "channel.chat.message",
        "version": "1",
        "condition": {
            "broadcaster_user_id": """+OSTConfiguration.settings.getTwitchUser().getId()
                            +"""
            "user_id":"""+OSTConfiguration.settings.getBotUser().getId()+"""
        },
        "transport": {
            "method": "webhook",
            "callback": "https://example.com/webhooks/callback",
            "secret": "s3cRe7"
        }
    }""",Object.class ))
                    .retrieve()
                    .body(String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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

    public static void putIntegerToByteArray(int i, byte[] array){
        array[0] = (byte)((i >> 24)& 0xFF);
        array[1] = (byte)((i >> 16)& 0xFF);
        array[2] = (byte)((i >> 8)& 0xFF);
        array[3] = (byte)(i & 0xFF);
    }

    public static byte[] convertIntegerToByteArray(int i){
        byte[] bytes = new byte[4];
        bytes[0] = (byte)((i >> 24)& 0xFF);
        bytes[1] = (byte)((i >> 16)& 0xFF);
        bytes[2] = (byte)((i >> 8)& 0xFF);
        bytes[3] = (byte)(i & 0xFF);
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return bytes;
    }
    public static byte[] convertShortToByteArray(int i){
        byte[] bytes = new byte[2];
        bytes[0] = (byte)((i >> 8)& 0xFF);
        bytes[1] = (byte)(i & 0xFF);
        return bytes;
    }

    public static byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID convertBytesToUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }

    public static byte[] getNetworkBytesWithLenghForString(String string, Charset charset){
        byte[] stringAsBytes = string.getBytes(charset);
        byte[] intAsBytes = convertIntegerToByteArray(stringAsBytes.length);
        byte[] networkBytes = new byte[4+stringAsBytes.length];

        // add length first
        for (int i=0;i<intAsBytes.length;i++){
            networkBytes[i] = intAsBytes[i];
        }

        for (int i = 0; i < stringAsBytes.length; i++){
            networkBytes[i+4] = stringAsBytes[i];
        }
        return networkBytes;
     }

     public static int convertBytesToInt(byte[] bytes) {
         return ByteBuffer.wrap(bytes).getInt();
     }

    public static int convertBytesToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }

}
