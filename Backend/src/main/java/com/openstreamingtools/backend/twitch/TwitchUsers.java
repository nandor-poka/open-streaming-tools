package com.openstreamingtools.backend.twitch;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for a collection of Twitch user results from the Helix API.
 * Wraps an array of {@link TwitchUser} objects returned from the get-users endpoint.
 *
 * @see <a href="https://dev.twitch.tv/docs/api/reference/#get-users">Twitch Get Users API</a>
 */
@Getter
@Setter
public class TwitchUsers {
    /** Array of Twitch user objects from API response */
    TwitchUser[] data;
}
