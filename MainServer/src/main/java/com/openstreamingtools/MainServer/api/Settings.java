package com.openstreamingtools.MainServer.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Settings {
    private int showTrackDelay = 5;
    private int volumeThreshold = 75;
    private int sdRed = 0;
    private int sdGreen = 128;
    private int sdBlue = 255;
    private int faderRed = 0;
    private int faderGreen = 255;
    private int faderBlue = 128;
    private boolean twitchStatus = false;
    private OauthToken twitchToken;
}

