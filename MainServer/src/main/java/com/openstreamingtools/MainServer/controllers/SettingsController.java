package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.api.Settings;
import com.openstreamingtools.MainServer.config.Configuration;

import org.springframework.web.bind.annotation.*;

import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

import java.io.File;
import java.io.IOException;

@RestController
public class SettingsController {

    @GetMapping(value = "/getSettings", produces = "application/json")
    public @ResponseBody Settings getSettings() throws IOException {
        return Configuration.settings;
    }

    @PostMapping(value = "/saveSettings", consumes = "application/json")
    public void postSettings(@RequestBody String jsonString) throws IOException {
        Settings tempSettings = objectMapper.readValue(jsonString, Settings.class);
        objectMapper.writeValue(Configuration.getSettingsFile(), tempSettings);
        Configuration.settings = objectMapper.readValue(Configuration.getSettingsFile(), Settings.class);
    }
}
