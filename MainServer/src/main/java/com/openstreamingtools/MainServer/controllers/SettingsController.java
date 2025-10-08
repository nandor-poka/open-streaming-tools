package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.api.Settings;
import com.openstreamingtools.MainServer.config.OSTConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

@RestController
@Slf4j
public class SettingsController {

    @Autowired
    Environment env;

    @GetMapping(value = "/api/getSettings", produces = "application/json")
    public @ResponseBody Settings getSettings() throws IOException {
        if (OSTConfiguration.settings.getVersionString().isBlank()){
            OSTConfiguration.settings.setVersionString(env.getProperty("versionString"));
            OSTConfiguration.saveSettings();
        }
        return OSTConfiguration.settings;
    }

    @PostMapping(value = "/api/saveSettings", consumes = "application/json")
    public void postSettings(@RequestBody String jsonString) throws IOException {
        OSTConfiguration.settings = objectMapper.readValue(jsonString, Settings.class);
        OSTConfiguration.saveSettings();
    }
}
