package com.openstreamingtools.backend.twitch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessage {
    private String broadcaster_id;
    private String sender_id;
    private String message;
}
