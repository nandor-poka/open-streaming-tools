package com.openstreamingtools.backend.twitch;


import lombok.Data;

@Data
public class TwitchSubscriptionTransport {
    private String method = "websocket";
    private String session_id;

    public TwitchSubscriptionTransport(String session_id) {
        this.session_id = session_id;
    }
}
