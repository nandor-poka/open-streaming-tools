package com.openstreamingtools.MainServer.twitch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Automatically starts Twitch WebSocket connections after server startup
 * Note: This is now redundant with ApplicationStartupListener but kept for compatibility
 */
@Component
@Slf4j
public class TwitchWebSocketAutoStarter implements ApplicationListener<ApplicationReadyEvent> {

    private final BotTwitchWebSocketClient botTwitchWebSocketClient;
    private final BroadcasterTwitchWebSocketClient broadcasterTwitchWebSocketClient;

    @Autowired
    public TwitchWebSocketAutoStarter(BotTwitchWebSocketClient botTwitchWebSocketClient,
                                     BroadcasterTwitchWebSocketClient broadcasterTwitchWebSocketClient) {
        this.botTwitchWebSocketClient = botTwitchWebSocketClient;
        this.broadcasterTwitchWebSocketClient = broadcasterTwitchWebSocketClient;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("🚀 Server startup complete - Twitch WebSocket auto-starter (redundant with ApplicationStartupListener)");

        try {
            // Small delay to ensure all beans are fully initialized
            Thread.sleep(2000);

            log.info("🔌 TwitchWebSocketAutoStarter: Checking WebSocket connections...");

            // Note: WebSocket connections are now handled by ApplicationStartupListener
            // This class is kept for backward compatibility but connections happen earlier in the startup process

            log.info("✅ Twitch WebSocket auto-starter completed (connections handled by ApplicationStartupListener)");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("❌ Interrupted while waiting for Twitch WebSocket initialization", e);
        } catch (Exception e) {
            log.error("❌ Failed to initialize Twitch WebSocket connection: {}", e.getMessage(), e);
        }
    }
}
