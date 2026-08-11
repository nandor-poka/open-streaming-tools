package com.openstreamingtools.MainServer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstreamingtools.MainServer.messages.MessageFromFrontend;
import com.openstreamingtools.MainServer.twitch.ChatMessage;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import com.openstreamingtools.MainServer.twitch.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

@RestController
@Slf4j
public class ChatMessageController {

    @PostMapping(value = "/api/shoutout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendShoutOut(@RequestBody String jsonString) throws JsonProcessingException {
        MessageFromFrontend message = objectMapper.readValue(jsonString, MessageFromFrontend.class);
        TwitchUtils.sendToChat(TwitchUtils.SHOUTOUT_COMMAND + message.getMessage(), UserType.BROADCASTER);
    }

    @PostMapping(value = "/api/chat/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendChatMessage(@RequestBody String jsonString) throws JsonProcessingException {
        log.info("💬 Chat message received via HTTP API");
        log.info("📨 Payload: {}", jsonString);

        MessageFromFrontend message = objectMapper.readValue(jsonString, MessageFromFrontend.class);
        String messageText = message.getMessage();

        log.info("📝 Extracted message: '{}'", messageText);

        if (messageText == null || messageText.trim().isEmpty()) {
            log.warn("⚠️ Empty message received, ignoring");
            return;
        }

        log.info("✅ Message validation passed, message length: {}", messageText.length());
        log.debug("📤 Calling TwitchUtils.sendToChat()");

        TwitchUtils.sendToChat(messageText, UserType.BROADCASTER);

        log.info("✅ Chat message sent successfully to Twitch");
    }
}