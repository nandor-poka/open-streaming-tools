package com.openstreamingtools.MainServer.messages.frontend;

import com.openstreamingtools.MainServer.messages.MessageToFrontend;
import com.openstreamingtools.MainServer.messages.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongData extends MessageToFrontend {

    private int deckNumber;
    private String trackTitle;
    private String artistName;
    private int key;


    public SongData(int deckNumber, String trackTitle, String artistName) {
        super();
        this.type =MessageType.SONG_DATA;
        this.deckNumber = deckNumber;
        this.trackTitle = trackTitle;
        this.artistName = artistName;

    }
}
