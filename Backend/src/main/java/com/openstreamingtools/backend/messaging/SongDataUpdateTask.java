package com.openstreamingtools.backend.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.dj.stagelinq.SimpleState;
import com.openstreamingtools.backend.messages.frontend.SongData;
import com.openstreamingtools.backend.services.stagelinq.StateMapService;
import com.openstreamingtools.backend.utils.Utils;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.TimerTask;

/**
 * Timer task for scheduling and processing song data updates.
 * Updates deck state information and broadcasts song data to connected frontends.
 * Automatically removes itself from the scheduled tasks queue upon execution.
 */
@Getter
public class SongDataUpdateTask extends TimerTask {
    /** Song data to be processed and broadcast */
    private final SongData songData;
    /** Timestamp when this task was created */
    private final Instant timestamp;

    /**
     * Creates a new song data update task.
     *
     * @param songData the song data to process
     * @param timestamp the creation timestamp
     */
    public SongDataUpdateTask(SongData songData, Instant timestamp) {
        this.songData = songData;
        this.timestamp = timestamp;
     }

    /**
     * Executes the task: updates deck state, sends song data to frontend, and queues for logging.
     * Sets the IS_SHOWING flag based on whether artist or track information is present.
     *
     * @throws RuntimeException if JSON serialization fails
     */
    @Override
    public void run() {
        try {
            StateMapService.deckStates.get(songData.getDeckNumber())
                    .put(SimpleState.LAST_UPDATE, System.currentTimeMillis());
            Utils.removeScheduledTask(this);
            if (!songData.getArtistName().isBlank() || !songData.getTrackTitle().isBlank()){
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
