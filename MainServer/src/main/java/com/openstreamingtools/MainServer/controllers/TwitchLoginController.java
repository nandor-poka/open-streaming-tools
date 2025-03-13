package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.api.OauthToken;
import com.openstreamingtools.MainServer.config.OSTConfiguration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class TwitchLoginController {

    @GetMapping (value= "/twitch")
    public void twitchRedirect(@RequestParam String code,@RequestParam String scope){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id","n6breeyo2zy1nzlpfx43x91lgaobgo");
        params.add("client_secret", "y6gfc36ycbvt8z89rhxsaqk6hb649f");
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", "http://localhost:8080/");
        log.debug(code);
        OauthToken response = OSTConfiguration.restClient.post()
                .uri("https://id.twitch.tv/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .body(OauthToken.class);
        log.debug(response.toString());
        OSTConfiguration.settings.setTwitchToken(response);
        OSTConfiguration.saveSettings();

    }
}
