package com.openstreamingtools.MainServer.api;

import com.openstreamingtools.MainServer.twitch.TwitchUser;
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
    private String channelUserName;
    private String botUserName;
    private OauthToken twitchToken;
    private TwitchUser twitchUser;
    private TwitchUser botUser;
    private String shoutOutListFilePath;
    private String clientSecretFilePath;
    private String clientIdFilePath;
}

