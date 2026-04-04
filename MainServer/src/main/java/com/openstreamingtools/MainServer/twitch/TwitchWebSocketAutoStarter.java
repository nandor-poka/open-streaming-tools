package com.openstreamingtools.MainServer.twitch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Automatically starts Twitch WebSocket connection after server startup
 */
@Component
@Slf4j
public class TwitchWebSocketAutoStarter implements ApplicationListener<ApplicationReadyEvent> {

    private final TwitchWebSocketClient twitchWebSocketClient;

    @Autowired
    public TwitchWebSocketAutoStarter(TwitchWebSocketClient twitchWebSocketClient) {
        this.twitchWebSocketClient = twitchWebSocketClient;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("🚀 Server startup complete - initializing Twitch WebSocket connection");

        try {
            // Small delay to ensure all beans are fully initialized
            Thread.sleep(2000);

            log.info("🔌 Starting Twitch WebSocket connection...");
            twitchWebSocketClient.connect();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("❌ Interrupted while waiting for Twitch WebSocket initialization", e);
        } catch (Exception e) {
            log.error("❌ Failed to initialize Twitch WebSocket connection: {}", e.getMessage(), e);
        }
    }
}
