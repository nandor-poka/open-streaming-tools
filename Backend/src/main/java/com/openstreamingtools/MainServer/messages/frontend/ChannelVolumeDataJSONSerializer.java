package com.openstreamingtools.MainServer.messages.frontend;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ChannelVolumeDataJSONSerializer extends JsonSerializer<ChannelVolumeData> {

    @Override
    public void serialize(ChannelVolumeData deckState, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", deckState.getType().getName());
        jgen.writeObjectFieldStart("message");
        jgen.writeObjectField("deckNumber", deckState.getDeckNumber());
        jgen.writeObjectField("volume", deckState.getVolume());
        jgen.writeEndObject();
        jgen.writeEndObject();
    }
}
