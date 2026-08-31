package com.openstreamingtools.backend.twitch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for sending chat messages to Twitch.
 * Contains the broadcaster channel, sender ID, and message content
 * required for the Twitch Helix chat message API endpoint.
 */
@Getter
@Setter
@AllArgsConstructor
public class ChatMessage {
    /** The Twitch broadcaster/channel ID receiving the message */
    private String broadcaster_id;
    /** The user ID sending the message */
    private String sender_id;
    /** The chat message text to send */
    private String message;
}
