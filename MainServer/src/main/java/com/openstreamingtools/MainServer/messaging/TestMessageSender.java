package com.openstreamingtools.MainServer.messaging;

import java.util.TimerTask;

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
