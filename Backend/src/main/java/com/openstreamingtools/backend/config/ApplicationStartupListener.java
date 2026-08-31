package com.openstreamingtools.backend.config;


import com.openstreamingtools.backend.api.Settings;
import com.openstreamingtools.backend.twitch.BotTwitchWebSocketClient;
import com.openstreamingtools.backend.twitch.BroadcasterTwitchWebSocketClient;
import com.openstreamingtools.backend.twitch.TwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static com.openstreamingtools.backend.utils.Utils.objectMapper;

/**
 * Application startup listener that initializes settings and Twitch connections.
 * Listens for the Spring context refresh event and performs initialization tasks including:
 * <ul>
 *   <li>Creating settings directory and file if they don't exist</li>
 *   <li>Loading application configuration from settings file</li>
 *   <li>Refreshing Twitch OAuth tokens</li>
 *   <li>Initiating WebSocket connections for Bot and Broadcaster clients</li>
 * </ul>
 */
@Slf4j
@Component
public class ApplicationStartupListener implements
        ApplicationListener<ContextRefreshedEvent> {

    private final BotTwitchWebSocketClient botTwitchWebSocketClient;
    private final BroadcasterTwitchWebSocketClient broadcasterTwitchWebSocketClient;

    /**
     * Initializes the startup listener with WebSocket client references.
     *
     * @param botTwitchWebSocketClient the WebSocket client for bot authentication
     * @param broadcasterTwitchWebSocketClient the WebSocket client for broadcaster authentication
     */
    @Autowired
    public ApplicationStartupListener(BotTwitchWebSocketClient botTwitchWebSocketClient,
                                     BroadcasterTwitchWebSocketClient broadcasterTwitchWebSocketClient) {
        this.botTwitchWebSocketClient = botTwitchWebSocketClient;
        this.broadcasterTwitchWebSocketClient = broadcasterTwitchWebSocketClient;
    }



    /**
     * Handles the Spring context refresh event to perform startup initialization.
     * Creates necessary directories/files, loads settings, refreshes tokens, and connects WebSocket clients.
     *
     * @param event the ContextRefreshedEvent triggered by Spring on startup
     */
    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        File settingsFileDir = new File(OSTConfiguration.SETTINGS_DIR_PATH);
        if (!settingsFileDir.exists()) {
           if (!settingsFileDir.mkdirs()){
               log.error("Could not create directory: " + settingsFileDir.getAbsolutePath());
               return;
           }
        }
        File settingsFile = new File(OSTConfiguration.SETTINGS_FILE_PATH);
        if (!settingsFile.exists()) {
            try {
                if (settingsFile.createNewFile()){
                    objectMapper.writeValue(settingsFile, new Settings());
                }else{
                    log.error("Could not create file: " + settingsFile.getAbsolutePath());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        OSTConfiguration.setSettingsFile(settingsFile);
        OSTConfiguration.init();

        // Refresh tokens on startup before websocket connection
        log.info("🔄 Refreshing Twitch tokens on application startup...");

        boolean botRefreshSuccess = true;
        boolean broadcasterRefreshSuccess = true;

        if (OSTConfiguration.settings.getTwitchBotToken() != null) {
            botRefreshSuccess = TwitchUtils.refreshBotToken();
            if (botRefreshSuccess) {
                log.info("✅ Bot token refreshed successfully on startup");
            } else {
                log.warn("❌ Bot token refresh failed on startup");
            }
        } else {
            log.info("ℹ️ No bot token found, skipping bot token refresh");
            OSTConfiguration.settings.setBotTokenRefreshSuccess(true); // Not an error if no token exists
        }

        if (OSTConfiguration.settings.getTwitchBroadcasterToken() != null) {
            broadcasterRefreshSuccess = TwitchUtils.refreshBroadcasterToken();
            if (broadcasterRefreshSuccess) {
                log.info("✅ Broadcaster token refreshed successfully on startup");
            } else {
                log.warn("❌ Broadcaster token refresh failed on startup");
            }
        } else {
            log.info("ℹ️ No broadcaster token found, skipping broadcaster token refresh");
            OSTConfiguration.settings.setBroadcasterTokenRefreshSuccess(true); // Not an error if no token exists
        }

        // Save the refresh status
        OSTConfiguration.saveSettings();

        log.info("🔄 Token refresh complete - Bot: {}, Broadcaster: {}",
                 botRefreshSuccess ? "SUCCESS" : "FAILED",
                 broadcasterRefreshSuccess ? "SUCCESS" : "FAILED");

        // Connect WebSocket clients after token refresh
        log.info("🔌 Connecting WebSocket clients after token refresh...");

        if (botRefreshSuccess) {
            log.info("🔌 Connecting bot WebSocket client");
            botTwitchWebSocketClient.connect();
        } else {
            log.warn("⚠️ Skipping bot WebSocket connection - token refresh failed");
        }

        if (broadcasterRefreshSuccess) {
            log.info("🔌 Connecting broadcaster WebSocket client");
            broadcasterTwitchWebSocketClient.connect();
        } else {
            log.warn("⚠️ Skipping broadcaster WebSocket connection - token refresh failed");
        }

        log.info("✅ Application startup initialization complete");

    }
}
