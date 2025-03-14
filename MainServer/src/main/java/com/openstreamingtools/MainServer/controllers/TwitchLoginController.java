package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.api.OauthToken;
import com.openstreamingtools.MainServer.config.OSTConfiguration;

import com.openstreamingtools.MainServer.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class TwitchLoginController {

    @GetMapping (value= "/twitch")
    public String twitchRedirect(@RequestParam String code,@RequestParam String scope){
        Utils.getAuthTokenFromTwitch(code);
        return "forward:/";
    }
}
