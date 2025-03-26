package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.api.Settings;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

@RestController
public class SettingsController {

    @GetMapping(value = "/api/getSettings", produces = "application/json")
    public @ResponseBody Settings getSettings() throws IOException {
        return OSTConfiguration.settings;
    }

    @PostMapping(value = "/api/saveSettings", consumes = "application/json")
    public void postSettings(@RequestBody String jsonString) throws IOException {
        OSTConfiguration.settings = objectMapper.readValue(jsonString, Settings.class);
        OSTConfiguration.saveSettings();
    }
}
