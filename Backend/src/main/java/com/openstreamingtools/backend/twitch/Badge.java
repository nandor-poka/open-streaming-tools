package com.openstreamingtools.backend.twitch;

import lombok.Data;

/**
 * Data transfer object for Twitch chat badges.
 * Represents a badge displayed on a user's chat message (e.g., moderator, subscriber).
 */
@Data
public class Badge {
    /** Badge set identifier */
    private String setId;
    /** Badge ID within the set */
    private String id;
    /** Badge version or info (e.g., subscription tier) */
    private String info;
}
