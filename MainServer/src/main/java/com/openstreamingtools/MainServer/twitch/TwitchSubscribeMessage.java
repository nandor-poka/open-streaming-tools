package com.openstreamingtools.MainServer.twitch;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TwitchSubscribeMessage {
    private String type="channel.chat.message";
    private String version = "1";
    TwitchSubscribeCondition condition;
    TwitchSubscriptionTransport transport;

    public TwitchSubscribeMessage(TwitchSubscribeCondition condition, TwitchSubscriptionTransport transport) {
        this.condition = condition;
        this.transport = transport;
    }
}


