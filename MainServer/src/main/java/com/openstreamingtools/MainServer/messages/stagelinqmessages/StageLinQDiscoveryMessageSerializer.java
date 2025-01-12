package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class StageLinQDiscoveryMessageSerializer extends JsonSerializer<StageLinqDiscoveryMessage> {

    @Override
    public void serialize(StageLinqDiscoveryMessage value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("action", value.getAction().toString());
        jgen.writeStringField("modelType", value.getModelType().toString());
        jgen.writeStringField("version", value.getSoftwareVersion());
        jgen.writeEndObject();
    }

    //    public static class Deserializer extends JsonDeserializer<StageLinqDiscoveryMessage> {
//
//        @Override
//        public MyObject deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
//            ObjectCodec codec = jsonParser.getCodec();
//            JsonNode tree = codec.readTree(jsonParser);
//            String name = tree.get("name").textValue();
//            int age = tree.get("age").intValue();
//            return new MyObject(name, age);
//        }
//
//    }
}
