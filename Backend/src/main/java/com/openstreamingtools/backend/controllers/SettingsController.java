package com.openstreamingtools.backend.controllers;

import com.openstreamingtools.backend.api.Settings;
import com.openstreamingtools.backend.config.OSTConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.openstreamingtools.backend.utils.Utils.objectMapper;

/**
 * REST controller for managing application settings.
 * Provides endpoints to retrieve and persist application configuration
 * including Twitch tokens, user preferences, and system properties.
 */
@RestController
@Slf4j
public class SettingsController {

    /** Spring Environment for accessing application properties */
    @Autowired
    Environment env;

    /**
     * Retrieves the current application settings.
     * Initializes version string from environment if not already set.
     *
     * @return the current {@link Settings} object containing all configuration
     * @throws IOException if settings cannot be loaded
     */
    @GetMapping(value = "/api/getSettings", produces = "application/json")
    public @ResponseBody Settings getSettings() throws IOException {
        if (OSTConfiguration.settings.getVersionString().isBlank()){
            OSTConfiguration.settings.setVersionString(env.getProperty("versionString"));
            OSTConfiguration.saveSettings();
        }
        return OSTConfiguration.settings;
    }

    /**
     * Saves application settings from a JSON payload.
     * Parses the JSON, updates the configuration object, and persists to disk.
     *
     * @param jsonString JSON representation of the settings to save
     * @throws IOException if settings cannot be saved or JSON cannot be parsed
     */
    @PostMapping(value = "/api/saveSettings", consumes = "application/json")
    public void postSettings(@RequestBody String jsonString) throws IOException {
        OSTConfiguration.settings = objectMapper.readValue(jsonString, Settings.class);
        OSTConfiguration.saveSettings();
    }
}
