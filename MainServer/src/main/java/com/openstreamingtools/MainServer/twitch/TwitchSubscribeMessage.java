package com.openstreamingtools.MainServer.twitch;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class TwitchSubscribeMessage {
    private String type="";
    private String version = "1";
    private TwitchSubscribtionCondition condition;
    private TwitchSubscriptionTransport transport;

    public TwitchSubscribeMessage(){}

}


