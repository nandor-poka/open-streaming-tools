package com.openstreamingtools.MainServer.twitch;


import lombok.Data;

@Data
public class Reward {
    private String id;
    private String title;
    private String prompt;
    private int cost;
}
