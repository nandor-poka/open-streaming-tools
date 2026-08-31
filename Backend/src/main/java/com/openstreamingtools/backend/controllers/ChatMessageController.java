package com.openstreamingtools.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.messages.MessageFromFrontend;
import com.openstreamingtools.backend.twitch.TwitchUtils;
import com.openstreamingtools.backend.twitch.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.openstreamingtools.backend.utils.Utils.objectMapper;

/**
 * REST controller for sending chat messages to Twitch.
 * Provides HTTP endpoints for sending regular chat messages and shoutouts
 * to the broadcaster's Twitch channel via the Twitch API.
 */
@RestController
@Slf4j
public class ChatMessageController {

    /**
     * Sends a shoutout message to the Twitch chat.
     * Receives a chat message from the frontend and prepends the shoutout command.
     *
     * @param jsonString JSON payload containing the message text
     * @throws JsonProcessingException if the JSON payload cannot be parsed
     */
    @PostMapping(value = "/api/shoutout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendShoutOut(@RequestBody String jsonString) throws JsonProcessingException {
        MessageFromFrontend message = objectMapper.readValue(jsonString, MessageFromFrontend.class);
        TwitchUtils.sendToChat(TwitchUtils.SHOUTOUT_COMMAND + message.getMessage(), UserType.BROADCASTER);
    }

    /**
     * Sends a regular chat message to the Twitch chat.
     * Receives a message from the frontend, validates it, and sends it to the broadcaster's channel.
     * Ignores empty or null messages.
     *
     * @param jsonString JSON payload containing the message text
     * @throws JsonProcessingException if the JSON payload cannot be parsed
     */
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