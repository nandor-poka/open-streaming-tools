package com.openstreamingtools.MainServer.twitch;

import lombok.Getter;
import lombok.Setter;

/*
https://dev.twitch.tv/docs/api/reference/#get-users
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
