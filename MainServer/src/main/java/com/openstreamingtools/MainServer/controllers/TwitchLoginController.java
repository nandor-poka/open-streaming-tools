package com.openstreamingtools.MainServer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.api.OauthToken;
import com.openstreamingtools.MainServer.config.OSTConfiguration;

import com.openstreamingtools.MainServer.utils.Utils;
import com.openstreamingtools.MainServer.websocket.WeboscketClient;
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
        if(OSTConfiguration.settings.getTwitchUser() == null){
            try {
                OSTConfiguration.settings.setTwitchUser(Utils.getIdforUser("NAND_OR_DNB").getData()[0]);
                OSTConfiguration.saveSettings();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        if(OSTConfiguration.settings.getBotUser() == null){
            try {
                OSTConfiguration.settings.setBotUser(Utils.getIdforUser("OSTBot").getData()[0]);
                OSTConfiguration.saveSettings();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        WeboscketClient.connect();
        return "forward:/";
    }
}
