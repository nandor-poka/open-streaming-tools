package com.openstreamingtools.backend.twitch;

import lombok.Data;

/**
 * Generic data transfer object for EventSub WebSocket messages.
 * Wraps subscription metadata and the event payload received from Twitch.
 *
 * @param <T> the type of event data
 */
@Data
public class TwitchWebsocketMessage<T> {
    /** Subscription information that generated this event */
    private TwitchWebsocketMessageSubscription subscription;
    /** The event data payload (type varies by event type) */
    private T event;
    /** Unique event identifier */
    private String id;     // Event ID
    /** ISO 8601 timestamp of the event */
    private String timestamp;
}
