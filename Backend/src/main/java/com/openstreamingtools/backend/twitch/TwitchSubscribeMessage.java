package com.openstreamingtools.backend.twitch;


import lombok.Data;


@Data
public class TwitchSubscribeMessage {
    private String type="";
    private String version = "1";
    private TwitchSubscribtionCondition condition;
    private TwitchSubscriptionTransport transport;

    public TwitchSubscribeMessage(){}

}


