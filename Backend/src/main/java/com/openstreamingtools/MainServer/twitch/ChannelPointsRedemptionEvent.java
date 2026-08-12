package com.openstreamingtools.MainServer.twitch;

import lombok.Data;

@Data
public class ChannelPointsRedemptionEvent {
    private String id;
    private String broadcasterUserId;
    private String broadcasterUserLogin;
    private String broadcasterUserName;
    private String userId;
    private String userLogin;
    private String userName;
    private String userInput;
    private String status;
    private Reward reward;
    private String redeemedAt;
}
