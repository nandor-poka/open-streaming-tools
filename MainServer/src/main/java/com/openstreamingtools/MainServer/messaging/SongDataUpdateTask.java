package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.utils.SongDataLogger;

import java.util.TimerTask;

public class SongDataUpdateTask extends TimerTask {
     private SongData songData;

     public SongDataUpdateTask(SongData songData) {
         this.songData = songData;
     }
    @Override
    public void run() {
        try {
            MessageSender.sendMessage(songData);
            SongDataLogger.logSongData(songData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
