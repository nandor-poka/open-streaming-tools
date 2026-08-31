package com.openstreamingtools.backend.messages.frontend;

import com.openstreamingtools.backend.messages.MessageToFrontend;
import com.openstreamingtools.backend.messages.MessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for song/track information sent to the frontend.
 * Extends MessageToFrontend to include track details like artist, title, musical key,
 * and the deck number where the track is playing.
 */
@Getter
@Setter
public class SongData extends MessageToFrontend {

    /** DJ deck/player number where this track is playing */
    private int deckNumber;
    /** Title of the track being played */
    private String trackTitle;
    /** Artist name of the track */
    private String artistName;
    /** Musical key of the track (0-11 representing semitones) */
    private int key;
    /** Timestamp when this song data was captured */
    private long timestamp;


    /**
     * Creates a SongData message with track information.
     *
     * @param deckNumber the deck number playing this track
     * @param trackTitle the track title
     * @param artistName the artist name
     * @param key the musical key of the track
     * @param timestamp the timestamp of data capture
     */
    public SongData(int deckNumber, String trackTitle, String artistName, Integer key, long timestamp) {
        super();
        this.type =MessageType.SONG_DATA;
        this.deckNumber = deckNumber;
        this.trackTitle = trackTitle;
        this.artistName = artistName;
        this.key = key;
        this.timestamp = timestamp;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SongData songData = (SongData) o;
        return deckNumber == songData.deckNumber
                && key == songData.key
                && trackTitle.equals(songData.trackTitle)
                && artistName.equals(songData.artistName);
    }

    @Override
    public int hashCode() {
        int result = deckNumber;
        result = 31 * result + trackTitle.hashCode();
        result = 31 * result + artistName.hashCode();
        result = 31 * result + key;
        return result;
    }

    @Override
    public String toString() {
        return "SongData{" +
                "deckNumber=" + deckNumber +
                ", trackTitle='" + trackTitle + '\'' +
                ", artistName='" + artistName + '\'' +
                ", key=" + key +
                '}';
    }
}
