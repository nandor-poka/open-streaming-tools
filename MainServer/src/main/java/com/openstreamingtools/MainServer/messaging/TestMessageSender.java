package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.dj.stagelinq.SimpleState;
import com.openstreamingtools.MainServer.messages.frontend.ChannelVolumeData;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.utils.SongDataLogger;

import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class TestMessageSender extends TimerTask {
    @Override
    public void run() {
       /* int deckNum =  ThreadLocalRandom.current().nextInt(1, 4 + 1);
        int tempo =  ThreadLocalRandom.current().nextInt(170, 180 + 1);
        int faderPos =  ThreadLocalRandom.current().nextInt(0, 100 + 1);

        SongData sd = new SongData(deckNum,
                ThreadLocalRandom.current().nextInt(0, 100 + 1) > 50 ? "exxxtra long song title to check word wrap hope this works "+deckNum :
                "short song title "+deckNum,
                "NAND/OR", (Integer) deckStates.get(deck).get(SimpleState.KEY));
        ChannelVolumeData cvd = new ChannelVolumeData(deckNum, faderPos);
        try {
            MessageSender.sendMessage(sd);
            MessageSender.sendMessage(cvd);
            SongDataLogger.logSongData(sd);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/
    }
}
