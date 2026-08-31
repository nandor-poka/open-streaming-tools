package com.openstreamingtools.backend.twitch;

import lombok.Data;

/**
 * Data transfer object representing a fragment of a Twitch chat message.
 * A message can contain multiple fragments such as text, emotes, cheermotes, or mentions.
 */
@Data
public class Fragment {
    /** Fragment type: "text", "emote", "cheermote", or "mention" */
    private String type;
    /** Text content of the fragment */
    private String text;
    /** Cheermote information if type is "cheermote" */
    private String cheermote;
    /** Emote information if type is "emote" */
    private String emote;
    /** Mentioned user information if type is "mention" */
    private String mention;
}
