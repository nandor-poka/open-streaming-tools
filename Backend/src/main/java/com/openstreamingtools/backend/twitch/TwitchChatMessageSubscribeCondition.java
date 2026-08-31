package com.openstreamingtools.backend.twitch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * EventSub subscription condition for chat message events.
 * Extends TwitchSubscribtionCondition to include user ID for chat message subscriptions.
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class TwitchChatMessageSubscribeCondition extends TwitchSubscribtionCondition {
    /** The user ID (typically the bot) to receive chat messages from */
    private String user_id;

}

