package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.config.Configuration;

import java.util.UUID;

public class TimestampReply {
    private UUID uuid; //uuid of the device to which this reply is sent
    byte[] actingAs = Configuration.STAGELINQ_ACTING_AS.getValue();
    long timestamp;

    public TimestampReply(UUID uuid) {
        this.uuid = uuid;
    }
}
