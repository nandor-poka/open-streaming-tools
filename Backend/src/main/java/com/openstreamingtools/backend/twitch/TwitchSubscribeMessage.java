package com.openstreamingtools.backend.twitch;


import lombok.Data;


/**
 * Data transfer object for Twitch EventSub subscription requests.
 * Contains subscription type, version, conditions, and transport information
 * required to subscribe to Twitch events via EventSub API.
 */
@Data
public class TwitchSubscribeMessage {
    /** EventSub event type to subscribe to (e.g., "channel.chat.message") */
    private String type="";
    /** EventSub API version (currently "1") */
    private String version = "1";
    /** Conditions for filtering events (e.g., broadcaster ID, user ID) */
    private TwitchSubscribtionCondition condition;
    /** Transport configuration specifying WebSocket delivery method */
    private TwitchSubscriptionTransport transport;

    /**
     * Default constructor for EventSub subscription message.
     */
    public TwitchSubscribeMessage(){}

}

