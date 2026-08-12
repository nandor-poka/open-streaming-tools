package com.openstreamingtools.MainServer.api;


import lombok.Data;

import java.util.ArrayList;

@Data
public class OauthToken {
    private String access_token = "";
    private int expires_in = 0;
    private String refresh_token = "";
    private ArrayList<String> scope = new ArrayList<String>();
    private String token_type = "bearer";
}
