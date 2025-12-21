package com.openstreamingtools.MainServer.twitch;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
public class TwitchSubscriptionTransport {
    private String method = "websocket";
    private String session_id;

    public TwitchSubscriptionTransport(String session_id) {
        this.session_id = session_id;
    }
}
