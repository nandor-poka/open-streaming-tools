package com.openstreamingtools.backend.api;

import com.openstreamingtools.backend.twitch.TwitchUser;
import lombok.Getter;
import lombok.Setter;

/**
 * Application configuration and settings data transfer object.
 * Stores user preferences, authentication tokens, and system configuration.
 * Persisted to settings file and loaded at application startup.
 */
@Setter
@Getter
public class Settings {
    /** Delay in seconds before displaying track information after detection */
    private int showTrackDelay = 5;
    /** Volume threshold percentage for triggering updates */
    private int volumeThreshold = 75;
    /** Red component (0-255) for SD player visualization color */
    private int sdRed = 0;
    /** Green component (0-255) for SD player visualization color */
    private int sdGreen = 128;
    /** Blue component (0-255) for SD player visualization color */
    private int sdBlue = 255;
    /** Red component (0-255) for fader visualization color */
    private int faderRed = 0;
    /** Green component (0-255) for fader visualization color */
    private int faderGreen = 255;
    /** Blue component (0-255) for fader visualization color */
    private int faderBlue = 128;
    /** Whether Twitch integration is enabled and connected */
    private boolean twitchStatus = false;
    /** Twitch channel owner/broadcaster username */
    private String channelUserName;
    /** Twitch bot account username */
    private String botUserName;
    /** File path for auto-shoutout targets list */
    private String shoutOutListFilePath;
    /** File path for Twitch client secret */
    private String clientSecretFilePath;
    /** File path for Twitch client ID */
    private String clientIdFilePath;
    /** Comma-separated list of usernames for auto-shoutout */
    private String autoShoutoutList;
    /** Database playlist ID being used */
    private int playlistID;
    /** Application version string */
    private String versionString = "";
    /** Twitch OAuth token for bot account */
    private OauthToken twitchBotToken;
    /** Twitch OAuth token for broadcaster account */
    private OauthToken twitchBroadcasterToken;
    /** Broadcaster user profile information */
    private TwitchUser twitchUser;
    /** Bot user profile information */
    private TwitchUser botUser;
    /** Whether bot token refresh was successful */
    private boolean botTokenRefreshSuccess = true;
    /** Whether broadcaster token refresh was successful */
    private boolean broadcasterTokenRefreshSuccess = true;
}

