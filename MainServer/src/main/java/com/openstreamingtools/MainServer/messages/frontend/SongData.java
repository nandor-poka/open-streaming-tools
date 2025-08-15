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
        if (!(o instanceof SongData songData)) return false;
        return deckNumber == songData.deckNumber && key == songData.key && Objects.equals(trackTitle, songData.trackTitle) && Objects.equals(artistName, songData.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckNumber, trackTitle, artistName, key);
    }
}
