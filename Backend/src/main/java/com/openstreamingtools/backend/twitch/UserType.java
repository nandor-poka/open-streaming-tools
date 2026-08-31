package com.openstreamingtools.backend.twitch;

/**
 * Enumeration of Twitch user types for authentication and API interactions.
 * Distinguishes between bot and broadcaster accounts that have different permissions.
 */
public enum UserType {
    /** Bot user account with limited permissions (chat only) */
    BOT,
    /** Broadcaster/channel owner account with extended permissions */
    BROADCASTER
}
