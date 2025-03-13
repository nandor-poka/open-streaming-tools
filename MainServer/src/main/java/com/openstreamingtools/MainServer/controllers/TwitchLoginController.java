package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.api.OauthToken;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TwitchLoginController {

    @GetMapping(value= "/twitch", consumes = "application/x-www-form-urlencoded")
    public void twitchRedirect(@RequestParam String code,@RequestParam String scope,
    @RequestParam String state){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id","n6breeyo2zy1nzlpfx43x91lgaobgo");
        params.add("client_secret", "y6gfc36ycbvt8z89rhxsaqk6hb649f");
        params.add("grant_type", "client_credentials");

        OauthToken response = OSTConfiguration.restClient.post()
                .uri("https://id.twitch.tv/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .body(OauthToken.class);
        log.debug(String.valueOf(response));
        OSTConfiguration.settings.setTwitchToken(response);

    }
}
