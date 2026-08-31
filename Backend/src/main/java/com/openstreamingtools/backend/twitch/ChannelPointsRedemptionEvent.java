package com.openstreamingtools.backend.twitch;

import lombok.Data;

/**
 * Data transfer object for Twitch channel points redemption events.
 * Represents a user redeeming channel points for a custom reward on the broadcaster's channel.
 */
@Data
public class ChannelPointsRedemptionEvent {
    /** Unique redemption event ID */
    private String id;
    /** Broadcaster account ID */
    private String broadcasterUserId;
    /** Broadcaster login name */
    private String broadcasterUserLogin;
    /** Broadcaster display name */
    private String broadcasterUserName;
    /** User ID performing the redemption */
    private String userId;
    /** User login name */
    private String userLogin;
    /** User display name */
    private String userName;
    /** Text input provided by the user during redemption */
    private String userInput;
    /** Redemption status (e.g., "fulfilled", "canceled") */
    private String status;
    /** The reward being redeemed */
    private Reward reward;
    /** Timestamp when the redemption occurred */
    private String redeemedAt;
}
