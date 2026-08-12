package com.openstreamingtools.MainServer.services.stagelinq;

import java.util.UUID;

public class BeatInfoService {

    private byte[] beatInfoRequest = new byte[]{0x0, 0x0, 0x0, 0x4, 0x0, 0x0, 0x0, 0x0};
    private UUID deviceId;
    private long clock;
    private int deckCount;
}
