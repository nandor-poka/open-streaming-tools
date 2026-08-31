package com.openstreamingtools.backend.messages.stagelinqmessages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BeatInfoMessage {

    public BeatInfoMessage(){};

    public BeatInfoMessage(int id, long clock, int deckCount, BeatData[] decks) {
        this.id = id;
        this.clock = clock;
        this.deckCount = deckCount;
        this.decks = decks;
        valid = true;
    }

    private boolean valid = false;
    int id;
    private long clock;
    private int deckCount;
    private BeatData[] decks;

}
