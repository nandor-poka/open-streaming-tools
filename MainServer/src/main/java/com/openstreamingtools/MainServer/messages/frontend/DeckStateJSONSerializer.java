package com.openstreamingtools.MainServer.messages.frontend;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class DeckStateJSONSerializer extends JsonSerializer<DeckState> {


    /*
        private int deckNumber;
    private String trackTitle;
    private String trackArtist;
    private int tempo;
    private int faderPos;
     */
    @Override
    public void serialize(DeckState deckState, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", deckState.getType().getName());
        jgen.writeObjectFieldStart("message");
        jgen.writeObjectField("deckNumber", deckState.getDeckNumber());
        jgen.writeObjectField("trackTitle", deckState.getTrackTitle());
        jgen.writeObjectField("artistName", deckState.getArtistName());
        jgen.writeObjectField("tempo", deckState.getTempo());
        jgen.writeObjectField("faderPos", deckState.getFaderPos());
        jgen.writeEndObject();
        jgen.writeEndObject();
    }

}
