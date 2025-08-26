package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.dj.stagelinq.SimpleState;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.SongDataLogger;
import com.openstreamingtools.MainServer.utils.Utils;

import java.util.Objects;
import java.util.TimerTask;

public class SongDataUpdateTask extends TimerTask {
     private final SongData songData;

    public SongDataUpdateTask(SongData songData) {
         this.songData = songData;
     }
    @Override
    public void run() {
        try {
            StateMapService.deckStates.get(songData.getDeckNumber())
                    .put(SimpleState.LAST_UPDATE, System.currentTimeMillis());
            MessageSender.sendMessage(songData);
            SongDataLogger.logSongData(songData);
            Utils.removeScheduledTask(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SongDataUpdateTask that = (SongDataUpdateTask) o;
        return this.songData.equals(that.songData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(songData);
    }

    @Override
    public String toString() {
        return "SongDataUpdateTask{" +
                "songData=" + songData +
                '}';
    }
}
