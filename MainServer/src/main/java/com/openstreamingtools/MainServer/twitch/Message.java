package com.openstreamingtools.MainServer.twitch;

import lombok.Data;

import java.util.List;

@Data
public class Message {
    private String text;
    private List<Fragment> fragments;
}
