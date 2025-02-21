package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.api.Settings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettignsController {

    @GetMapping(value = "/getSettings", produces = "application/json")
    public @ResponseBody Settings getSettings(){
        return new Settings();
    }

    @PostMapping("/postSettings")
    public void postSettings(){

    }
}
