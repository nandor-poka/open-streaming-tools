package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.messages.frontend.DeckState;
import org.springframework.integration.support.locks.RenewableLockRegistry;

import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class TestMessageSender extends TimerTask {
    @Override
    public void run() {
        int deckNum =  ThreadLocalRandom.current().nextInt(1, 4 + 1);
        int tempo =  ThreadLocalRandom.current().nextInt(170, 180 + 1);
        int faderPos =  ThreadLocalRandom.current().nextInt(0, 100 + 1);

        DeckState ds = new DeckState(deckNum,
                ThreadLocalRandom.current().nextInt(0, 100 + 1) > 50 ? "exxxtra long song title to check word wrap hope this works "+deckNum :
                "short song title "+deckNum,
                "NAND/OR", tempo, faderPos);
        try {
            MessageSender.sendMessage(ds);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
