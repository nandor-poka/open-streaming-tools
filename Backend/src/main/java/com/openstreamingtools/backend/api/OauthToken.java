package com.openstreamingtools.backend.api;


import lombok.Data;

import java.util.ArrayList;

/**
 * Data transfer object representing an OAuth token response from Twitch.
 * Contains access token, refresh token, expiration, scopes, and token type.
 * Fields are automatically handled by Lombok @Data annotation.
 */
@Data
public class OauthToken {
    /** OAuth access token for API authentication */
    private String access_token = "";
    /** Token expiration time in seconds */
    private int expires_in = 0;
    /** OAuth refresh token for obtaining new access tokens */
    private String refresh_token = "";
    /** List of authorized OAuth scopes for this token */
    private ArrayList<String> scope = new ArrayList<String>();
    /** Token type (typically "bearer") */
    private String token_type = "bearer";
}
