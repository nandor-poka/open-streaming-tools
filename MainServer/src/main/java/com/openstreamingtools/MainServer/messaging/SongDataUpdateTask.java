package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.dj.stagelinq.SimpleState;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.SongDataLogger;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;

@Getter
public class SongDataUpdateTask extends TimerTask {
     private final SongData songData;
     private final Instant timestamp;

    public SongDataUpdateTask(SongData songData, Instant timestamp) {
         this.songData = songData;
         this.timestamp = timestamp;
     }

    @Override
    public void run() {
        try {
            StateMapService.deckStates.get(songData.getDeckNumber())
                    .put(SimpleState.LAST_UPDATE, System.currentTimeMillis());
            Utils.removeScheduledTask(this);
            if (!songData.getArtistName().equals(" ") || !songData.getTrackTitle().equals(" ")){
                StateMapService.deckStates.get(songData.getDeckNumber()).put(SimpleState.IS_SHOWING, true);
            } else {
                StateMapService.deckStates.get(songData.getDeckNumber()).put(SimpleState.IS_SHOWING, false);
            }
            MessageSender.sendMessage(songData);
            Utils.logDataQueue.offer(songData);
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
