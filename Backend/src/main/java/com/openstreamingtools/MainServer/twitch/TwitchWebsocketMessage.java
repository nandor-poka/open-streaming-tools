package com.openstreamingtools.MainServer.twitch;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class TwitchWebsocketMessage<T> {
    private TwitchWebsocketMessageSubscription subscription;
    private T event;
    private String id;     // Event ID
    private String timestamp;
}
