package com.openstreamingtools.MainServer.twitch;

import lombok.Data;

import java.util.List;

@Data
public class ChannelChatMessageEvent {
    private String broadcasterUserId;
    private String broadcasterUserLogin;
    private String broadcasterUserName;
    private String chatterUserId;
    private String chatterUserLogin;
    private String chatterUserName;
    private String messageId;
    private Message message;
    private String color;
    private List<Badge> badges;
    private String messageType;
    private String cheer;
    private String reply;
    private String channelPointsCustomRewardId;
    private String sourceBroadcasterUserId;
    private String sourceBroadcasterUserLogin;
    private String sourceBroadcasterUserName;
    private String sourceMessageId;
    private String sourceBadges;


}
