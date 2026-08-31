package com.openstreamingtools.backend.services.stagelinq;

import java.util.UUID;

/**
 * Service for handling beat and timing information from StageLinQ devices.
 * Manages beat information requests and synchronization with DJ equipment.
 */
public class BeatInfoService {

    /** Beat information request payload in StageLinQ protocol format */
    private byte[] beatInfoRequest = new byte[]{0x0, 0x0, 0x0, 0x4, 0x0, 0x0, 0x0, 0x0};
    /** UUID of the device providing beat information */
    private UUID deviceId;
    /** Clock reference for synchronization */
    private long clock;
    /** Number of decks available on the device */
    private int deckCount;
}
