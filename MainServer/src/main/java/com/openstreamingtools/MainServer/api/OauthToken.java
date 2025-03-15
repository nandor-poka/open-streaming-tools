package com.openstreamingtools.MainServer.api;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class OauthToken {
    private String access_token = "";
    private int expires_in = 0;
    private String refresh_token = "";
    private ArrayList<String> scopes = new ArrayList<String>();
    private String token_type = "bearer";
}
