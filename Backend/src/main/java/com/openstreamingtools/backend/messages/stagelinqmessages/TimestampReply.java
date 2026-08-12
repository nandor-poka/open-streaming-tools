package com.openstreamingtools.backend.messages.stagelinqmessages;

import com.openstreamingtools.backend.config.OSTConfiguration;

import java.util.UUID;

public class TimestampReply {
    private UUID uuid; //uuid of the device to which this reply is sent
    byte[] actingAs = OSTConfiguration.STAGELINQ_ACTING_AS.getValue();
    long timestamp;

    public TimestampReply(UUID uuid) {
        this.uuid = uuid;
    }
}
