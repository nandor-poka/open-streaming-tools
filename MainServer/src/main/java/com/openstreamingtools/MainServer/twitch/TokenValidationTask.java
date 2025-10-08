package com.openstreamingtools.MainServer.twitch;

import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Slf4j
public class TokenValidationTask extends TimerTask {

    @Override
    public void run() {
        log.debug("Twitch token validation was: {}", TwitchUtils.validateToken());
    }
}
