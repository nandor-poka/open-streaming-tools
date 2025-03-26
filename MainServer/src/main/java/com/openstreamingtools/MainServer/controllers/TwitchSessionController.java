package com.openstreamingtools.MainServer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.api.WebsocketSessionId;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class TwitchSessionController {

    @GetMapping (value= "/api/twitch")
    public String twitchRedirect(@RequestParam String code,@RequestParam String scope){
        TwitchUtils.getAuthTokenFromTwitch(code);
        if(OSTConfiguration.settings.getTwitchUser() == null){
            try {
                OSTConfiguration.settings.setTwitchUser(TwitchUtils.getIdforUser("NAND_OR_DNB").getData()[0]);
                OSTConfiguration.saveSettings();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        if(OSTConfiguration.settings.getBotUser() == null){
            try {
                OSTConfiguration.settings.setBotUser(TwitchUtils.getIdforUser("OSTBot").getData()[0]);
                OSTConfiguration.saveSettings();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return "forward:/";
    }
    @PostMapping(value= "/api/subscribeToTwtitch", consumes = "application/json")
    public void subscribeToEventSub(@RequestBody WebsocketSessionId websocketSessionId)  {
        TwitchUtils.subscribeToTwitch(websocketSessionId.getSessionId());
    }
}
