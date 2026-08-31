package com.openstreamingtools.backend.twitch;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object representing a Twitch user as returned by the Helix API.
 * Contains user profile information including ID, login name, display name, and media URLs.
 * Fields are automatically handled by Lombok @Getter/@Setter annotations.
 *
 * @see <a href="https://dev.twitch.tv/docs/api/reference/#get-users">Twitch Get Users API Documentation</a>
 */
@Getter
@Setter
public class TwitchUser {
    private String id;
    private String login;
    private String display_name;
    private String type;
    private String broadcaster_type;
    private String description;
    private String profile_image_url;
    private String offline_image_url;
    private int view_count;
    private String created_at;

}
