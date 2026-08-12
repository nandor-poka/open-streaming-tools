package com.openstreamingtools.backend.twitch;

import lombok.Data;

@Data
public class TwitchWebsocketMessage<T> {
    private TwitchWebsocketMessageSubscription subscription;
    private T event;
    private String id;     // Event ID
    private String timestamp;
}
