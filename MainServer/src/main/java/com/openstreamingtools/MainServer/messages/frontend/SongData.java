package com.openstreamingtools.MainServer.messages.frontend;

import com.openstreamingtools.MainServer.messages.MessageToFrontend;
import com.openstreamingtools.MainServer.messages.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SongData extends MessageToFrontend {

    private int deckNumber;
    private String trackTitle;
    private String artistName;
    private int key;


    public SongData(int deckNumber, String trackTitle, String artistName, Integer key) {
        super();
        this.type =MessageType.SONG_DATA;
        this.deckNumber = deckNumber;
        this.trackTitle = trackTitle;
        this.artistName = artistName;
        this.key = key;

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
