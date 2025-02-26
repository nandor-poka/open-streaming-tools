package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.api.Settings;
import com.openstreamingtools.MainServer.config.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;


/**
 * REST Controller to allow the frontend to get and save its own settings.
 */
@RestController
public class SettingsController {

    @GetMapping(value = "/getSettings", produces = "application/json")
    public @ResponseBody Settings getSettings() throws IOException {
        return Configuration.settings;
    }

    @PostMapping(value = "/saveSettings", consumes = "application/json")
    public void postSettings(String jsonString) throws IOException {
        objectMapper.writeValue(Configuration.getSettingsFileResource().getFile(), jsonString);
        Configuration.settings = objectMapper.readValue(Configuration.getSettingsFileResource().getFile(), Settings.class);
    }
}
