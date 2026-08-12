package com.openstreamingtools.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.api.WebsocketSessionId;
import com.openstreamingtools.backend.config.OSTConfiguration;
import com.openstreamingtools.backend.twitch.BotTwitchWebSocketClient;
import com.openstreamingtools.backend.twitch.BroadcasterTwitchWebSocketClient;
import com.openstreamingtools.backend.twitch.TwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class TwitchSessionController {

    private final BotTwitchWebSocketClient botTwitchWebSocketClient;
    private final BroadcasterTwitchWebSocketClient broadcasterTwitchWebSocketClient;

    @Autowired
    public TwitchSessionController(BotTwitchWebSocketClient botTwitchWebSocketClient,
                                  BroadcasterTwitchWebSocketClient broadcasterTwitchWebSocketClient) {
        this.botTwitchWebSocketClient = botTwitchWebSocketClient;
        this.broadcasterTwitchWebSocketClient = broadcasterTwitchWebSocketClient;
    }

    @GetMapping (value= "/api/twitchBot")
    public String twitchBotRedirect(@RequestParam String code,@RequestParam String scope){
        log.debug(code);
        log.debug(scope);
        TwitchUtils.getAuthTokenFromTwitch(code, TwitchUtils.TwitchUserType.BOT);
        if(OSTConfiguration.settings.getBotUser() == null){
            try {
                OSTConfiguration.settings.setBotUser(
                        TwitchUtils.getIdforUser(OSTConfiguration.settings.getBotUserName())
                                .getData()[0]);
                OSTConfiguration.saveSettings();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        // Connect bot WebSocket client after successful authentication
        log.info("🔌 Connecting bot WebSocket client after authentication");
        botTwitchWebSocketClient.connect();

        return "redirect:/";
    }

    @GetMapping (value= "/api/twitchBroadcaster")
    public String twitchChannelRedirect(@RequestParam String code,@RequestParam String scope){
        log.debug("Broadcaster authentication code received");
        log.debug("Scope: {}", scope);

        // Authenticate broadcaster
        TwitchUtils.getAuthTokenFromTwitch(code, TwitchUtils.TwitchUserType.BROADCASTER);
        log.info("✅ Broadcaster authentication successful");

        // Set up user information if not already configured
        if(OSTConfiguration.settings.getTwitchUser() == null){
            try {
                log.info("Setting up broadcaster user information");
                OSTConfiguration.settings.setTwitchUser(
                        TwitchUtils.getIdforUser(OSTConfiguration.settings.getChannelUserName())
                                .getData()[0]);
                OSTConfiguration.saveSettings();
                log.info("✅ Broadcaster user configured: {}", OSTConfiguration.settings.getTwitchUser().getLogin());
            } catch (JsonProcessingException e) {
                log.error("❌ Failed to configure broadcaster user", e);
                throw new RuntimeException(e);
            }
        }

        // Connect broadcaster WebSocket client after successful authentication
        log.info("🔌 Connecting broadcaster WebSocket client after authentication");
        broadcasterTwitchWebSocketClient.connect();

        return "redirect:/";
    }

    @PostMapping(value= "/api/subscribeToTwtitch", consumes = "application/json")
    @Deprecated(since = "0.0.3", forRemoval = true)
    public String subscribeToEventSub(@RequestBody WebsocketSessionId websocketSessionId)  {
        log.warn("subscribeToEventSub called - this endpoint is deprecated. Subscriptions are now handled by backend WebSocket clients");
        // This endpoint is kept for backward compatibility but is no longer needed
        // Subscriptions are now automatically handled by WebSocket clients when session_welcome is received
        return TwitchUtils.subscribeToTwitch(websocketSessionId.getSessionId());
    }

    @GetMapping(value= "/api/getSubscriptions")
    public String getSubscriptions(){
        return TwitchUtils.getSubscriptions();
    }

    @GetMapping(value= "/api/twitchBotOAuthUrl")
    public String getTwitchBotOAuthUrl(){
        log.info("📋 Generating Twitch Bot OAuth URL");
        String oauthUrl = "https://id.twitch.tv/oauth2/authorize?" +
                "client_id=" + OSTConfiguration.getTWITCH_CLIEND_ID() +
                "&force_verify=true&response_type=code" +
                "&redirect_uri=http://localhost:8080/api/twitchBot" +
                "&scope=user%3Aread%3Achat%20user%3Abot%20user%3Awrite%3Achat";
        log.debug("✅ Bot OAuth URL generated");
        return oauthUrl;
    }

    @GetMapping(value= "/api/twitchBroadcasterOAuthUrl")
    public String getTwitchBroadcasterOAuthUrl(){
        log.info("📋 Generating Twitch Broadcaster OAuth URL");
        String oauthUrl = "https://id.twitch.tv/oauth2/authorize?" +
                "client_id=" + OSTConfiguration.getTWITCH_CLIEND_ID() +
                "&force_verify=true&response_type=code" +
                "&redirect_uri=http://localhost:8080/api/twitchBroadcaster" +
                "&scope=channel%3Amanage%3Aredemptions%20channel%3Aread%3Aredemptions%20user%3Aread%3Achat%20user%3Abot%20user%3Awrite%3Achat";
        log.debug("✅ Broadcaster OAuth URL generated");
        return oauthUrl;
    }
}
