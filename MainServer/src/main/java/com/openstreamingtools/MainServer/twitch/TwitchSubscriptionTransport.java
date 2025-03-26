package com.openstreamingtools.MainServer.twitch;


import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TwitchSubscriptionTransport {
    private String method = "websocket";
    private String session_id;

    public TwitchSubscriptionTransport(String session_id) {
        this.session_id = session_id;
    }
}
