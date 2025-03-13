package com.openstreamingtools.MainServer.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchWebsocketMessagePayloadTransport {
    private String method;
    private String session_id;
    private  String created_at;
}
