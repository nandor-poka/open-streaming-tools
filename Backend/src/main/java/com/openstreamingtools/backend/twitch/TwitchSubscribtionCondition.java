package com.openstreamingtools.backend.twitch;

import com.openstreamingtools.backend.config.OSTConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for EventSub subscription conditions.
 * Specifies the broadcaster channel for which to receive subscription events.
 */
@Data
@AllArgsConstructor
public class TwitchSubscribtionCondition {
    /** Twitch broadcaster user ID for the subscription condition */
    public String broadcaster_user_id;

    /**
     * Default constructor that uses the configured broadcaster from settings.
     */
    public TwitchSubscribtionCondition(){
        broadcaster_user_id = OSTConfiguration.settings.getTwitchUser().getId();
    }
}

