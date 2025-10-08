package com.openstreamingtools.MainServer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.messages.MessageFromFrontend;
import com.openstreamingtools.MainServer.twitch.ChatMessage;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

@RestController
public class ChatMessageController {

    @PostMapping(value = "/api/shoutout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendShoutOut(@RequestBody String jsonString) throws JsonProcessingException {
        MessageFromFrontend message = objectMapper.readValue(jsonString, MessageFromFrontend.class);
        TwitchUtils.sendToChat(TwitchUtils.SHOUTOUT_COMMAND + message.getMessage());
    }
}