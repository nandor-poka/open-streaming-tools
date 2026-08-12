package com.openstreamingtools.backend.twitch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchWebsocketMessageSubscription {
    private String id;
    private String status;
    private String type;
    private String version;
    private int cost;
    private TwitchSubscribtionCondition condition;
    private TwitchSubscriptionTransport transport;
    private String created_at;

}
