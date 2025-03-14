package com.openstreamingtools.MainServer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.api.OauthToken;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

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
    public static final String TWITCH_API_AUTHORIZE_URL = "https://id.twitch.tv/oauth2/authorize";
    public static final String TWITCH_EVENTSUB_WEBSOCKET_ADDRESS = "wss://eventsub.wss.twitch.tv/ws";
    public static RestClient restClient = RestClient.create();

    public static void getAuthTokenFromTwitch(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id",OSTConfiguration.TWITCH_CLIEND_ID);
        params.add("client_secret", OSTConfiguration.TWITCH_CLIENT_SECRET);
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/");
        log.debug(code);
        OauthToken response = Utils.restClient.post()
                .uri("https://id.twitch.tv/oauth2/token")
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

        OauthToken response = Utils.restClient.post()
                .uri("https://id.twitch.tv/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .body(OauthToken.class);
        log.debug(response.toString());
        OSTConfiguration.settings.setTwitchToken(response);
        OSTConfiguration.saveSettings();
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
