package com.openstreamingtools.backend.twitch;

import lombok.Data;

import java.util.List;

/**
 * Data transfer object for Twitch chat message content.
 * Contains the message text and a list of fragments that represent
 * different parts of the message (text, emotes, mentions, etc.).
 */
@Data
public class Message {
    /** The full message text */
    private String text;
    /** List of message fragments with metadata about formatting, emotes, etc. */
    private List<Fragment> fragments;
}
