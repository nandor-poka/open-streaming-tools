package com.openstreamingtools.MainServer.messages.frontend;

import com.fasterxml.jackson.core.JsonGenerator;
import com.openstreamingtools.MainServer.messages.MessageToFrontend;
import com.openstreamingtools.MainServer.messages.MessageType;

public class DeckState extends MessageToFrontend {

    private int deckNumber;
    private String trackTitle;
    private String artistName;
    private int tempo;
    private int faderPos;

    public DeckState( int deckNumber, String trackTitle, String artistName, int tempo, int faderPos) {
        super();
        this.type =MessageType.DECK_STATE;
        this.deckNumber = deckNumber;
        this.trackTitle = trackTitle;
        this.artistName = artistName;
        this.tempo = tempo;
        this.faderPos = faderPos;
    }

    public int getDeckNumber() {
        return deckNumber;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getTempo() {
        return tempo;
    }

    public int getFaderPos() {
        return faderPos;
    }
}
