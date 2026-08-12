package com.openstreamingtools.backend.messages.frontend;

import com.openstreamingtools.backend.messages.MessageToFrontend;
import com.openstreamingtools.backend.messages.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongData extends MessageToFrontend {

    private int deckNumber;
    private String trackTitle;
    private String artistName;
    private int key;
    private long timestamp;


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
