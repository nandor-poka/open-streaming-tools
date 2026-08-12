package com.openstreamingtools.backend.api;

import com.openstreamingtools.backend.twitch.TwitchUser;
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
    private String shoutOutListFilePath;
    private String clientSecretFilePath;
    private String clientIdFilePath;
    private String autoShoutoutList;
    private int playlistID;
    private String versionString = "";
    private OauthToken twitchBotToken;
    private OauthToken twitchBroadcasterToken;
    private TwitchUser twitchUser;
    private TwitchUser botUser;
    private boolean botTokenRefreshSuccess = true;
    private boolean broadcasterTokenRefreshSuccess = true;
}

