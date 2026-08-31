package com.openstreamingtools.backend.twitch;

import lombok.Data;

import java.util.List;

/**
 * Data transfer object for Twitch channel chat message events.
 * Represents a message sent in the broadcaster's chat channel,
 * including sender information, message content, and metadata.
 */
@Data
public class ChannelChatMessageEvent {
    /** Broadcaster account ID */
    private String broadcasterUserId;
    /** Broadcaster login name */
    private String broadcasterUserLogin;
    /** Broadcaster display name */
    private String broadcasterUserName;
    /** Chat message sender user ID */
    private String chatterUserId;
    /** Chat message sender login name */
    private String chatterUserLogin;
    /** Chat message sender display name */
    private String chatterUserName;
    /** Unique message identifier */
    private String messageId;
    /** Message content with fragments */
    private Message message;
    /** User's chat color (hex code) */
    private String color;
    /** List of chat badges (e.g., moderator, subscriber) */
    private List<Badge> badges;
    /** Message type (e.g., "text", "channel_points_highlighted") */
    private String messageType;
    /** Cheer information if message contains cheer bits */
    private String cheer;
    /** Reply information if this message is a reply */
    private String reply;
    /** Channel points custom reward ID if message was for a reward */
    private String channelPointsCustomRewardId;
    /** Source broadcaster ID for shared chat messages */
    private String sourceBroadcasterUserId;
    /** Source broadcaster login for shared chat messages */
    private String sourceBroadcasterUserLogin;
    /** Source broadcaster name for shared chat messages */
    private String sourceBroadcasterUserName;
    /** Source message ID for shared chat messages */
    private String sourceMessageId;
    /** Source message badges for shared chat messages */
    private String sourceBadges;


}
