package com.openstreamingtools.backend.messages.frontend;

import com.openstreamingtools.backend.messages.MessageToFrontend;
import com.openstreamingtools.backend.messages.MessageType;

public class ChannelVolumeData extends MessageToFrontend {
    private int deckNumber;
    private int volume;

    public ChannelVolumeData(int deckNum, int volume) {
        this.type = MessageType.CHANNEL_VOLUME_DATA;
        this.deckNumber = deckNum;
        this.volume = volume;
    }

    public int getDeckNumber() {
        return deckNumber;
    }

    public int getVolume() {
        return volume;
    }
}
