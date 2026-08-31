package com.openstreamingtools.backend.twitch;

import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * Periodic timer task for validating Twitch OAuth tokens.
 * Scheduled to run at regular intervals to ensure tokens remain valid
 * and can be refreshed before expiration.
 */
@Slf4j
public class TokenValidationTask extends TimerTask {

    /**
     * Validates the current Twitch token by calling the validation endpoint.
     * Logs the validation result for debugging purposes.
     */
    @Override
    public void run() {
        log.debug("Twitch token validation was: {}", TwitchUtils.validateToken());
    }
}
