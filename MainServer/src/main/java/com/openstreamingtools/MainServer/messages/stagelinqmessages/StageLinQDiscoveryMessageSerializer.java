package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class StageLinQDiscoveryMessageSerializer extends JsonSerializer<StageLinQDiscoveryMessage> {

    @Override
    public void serialize(StageLinQDiscoveryMessage value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", value.getType());
        jgen.writeStringField("message", value.getMessage());
        jgen.writeStringField("deviceID", value.getDeviceID().toString());
        jgen.writeStringField("action", value.getAction().toString());
        jgen.writeStringField("modelType", value.getModelType().toString());
        jgen.writeStringField("modelName", value.getModelCode().toString());
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
