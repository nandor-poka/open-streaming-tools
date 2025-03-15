package com.openstreamingtools.MainServer.twitch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchWebsocketMessagePayloadSession {
    private String id;
    private String status;
    private String connected_at;
    private int keepalive_timeout_seconds;
    private String reconnect_url;
}
