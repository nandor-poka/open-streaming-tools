package com.openstreamingtools.MainServer.twitch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

@Slf4j
public class ChatMessageSender {

    public static void sendToChat(String message){
        ChatMessage chatMessage = new ChatMessage("1109746665","1109746665",message);

        String respoonse = null;
        try {
            respoonse = Utils.restClient.post()
                    .uri(Utils.TWITCH_CHAT_MESSAGE)
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
