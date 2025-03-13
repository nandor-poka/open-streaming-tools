package com.openstreamingtools.MainServer.websocket;

import lombok.Getter;
import lombok.Setter;

/*
        "subscription": {
            "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
            "status": "enabled",
            "type": "channel.follow",
            "version": "1",
            "cost": 1,
            "condition": {
                "broadcaster_user_id": "12826"
            },
            "transport": {
                "method": "websocket",
                "session_id": "AQoQexAWVYKSTIu4ec_2VAxyuhAB"
            },
            "created_at": "2022-11-16T10:11:12.464757833Z"
        },
 */
@Getter
@Setter
public class TwitchWebsocketMessagePayloadSubscription {
    private String id;
    private String status;
    private String type;
    private String version;
    private int cost;

}
