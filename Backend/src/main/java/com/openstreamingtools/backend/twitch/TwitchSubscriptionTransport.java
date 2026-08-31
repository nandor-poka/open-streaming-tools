package com.openstreamingtools.backend.twitch;


import lombok.Data;

/**
 * Data transfer object for EventSub subscription transport configuration.
 * Specifies the WebSocket method and session ID for receiving subscription events.
 */
@Data
public class TwitchSubscriptionTransport {
    /** Transport method - "websocket" for EventSub subscriptions */
    private String method = "websocket";
    /** WebSocket session identifier for this subscription */
    private String session_id;

    /**
     * Creates a subscription transport for the given WebSocket session.
     *
     * @param session_id the WebSocket session ID
     */
    public TwitchSubscriptionTransport(String session_id) {
        this.session_id = session_id;
    }
}
