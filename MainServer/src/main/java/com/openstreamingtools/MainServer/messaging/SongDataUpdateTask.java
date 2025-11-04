package com.openstreamingtools.MainServer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.MainServer.dj.stagelinq.SimpleState;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.SongDataLogger;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.Getter;

import java.util.Objects;
import java.util.TimerTask;

@Getter
public class SongDataUpdateTask extends TimerTask {
     private final SongData[] songData = new SongData[4];
     private final long timestamp;

    public SongDataUpdateTask(SongData songData, long timestamp) {
         this.songData[0] = songData;
         this.timestamp = timestamp;
     }

    @Override
    public void run() {
        try {
            for (SongData songData : songData){
                StateMapService.deckStates.get(songData.getDeckNumber())
                        .put(SimpleState.LAST_UPDATE, System.currentTimeMillis());
                MessageSender.sendMessage(songData);

                Utils.removeScheduledTask(this);
                if (!songData.getArtistName().equals(" ") || !songData.getTrackTitle().equals(" ")){
                    StateMapService.deckStates.get(songData.getDeckNumber()).put(SimpleState.IS_SHOWING, true);
                } else {
                    StateMapService.deckStates.get(songData.getDeckNumber()).put(SimpleState.IS_SHOWING, false);
                }
            }
            SongDataLogger.logSongData(songData);
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
