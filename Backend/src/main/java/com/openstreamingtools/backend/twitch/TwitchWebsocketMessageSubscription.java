package com.openstreamingtools.backend.twitch;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for EventSub subscription metadata contained in WebSocket events.
 * Provides information about the subscription that generated a particular event.
 */
@Getter
@Setter
public class TwitchWebsocketMessageSubscription {
    /** Unique subscription identifier */
    private String id;
    /** Subscription status (e.g., "enabled", "webhook_callback_verification_pending") */
    private String status;
    /** Event type being subscribed to (e.g., "channel.chat.message") */
    private String type;
    /** EventSub API version */
    private String version;
    /** Cost in basis points for this subscription */
    private int cost;
    /** Conditions for this subscription */
    private TwitchSubscribtionCondition condition;
    /** Transport configuration for this subscription */
    private TwitchSubscriptionTransport transport;
    /** ISO 8601 timestamp when the subscription was created */
    private String created_at;

}
