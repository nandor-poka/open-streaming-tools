package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JacksonComponent;

import java.io.IOException;

@JacksonComponent
public class StageLinQDiscoveryMessageJSONSerializer extends JsonSerializer<StageLinQDiscoveryMessage> {

    @Override
    public void serialize(StageLinQDiscoveryMessage stageLinQDiscoveryMessage, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", stageLinQDiscoveryMessage.getType().getName());
        jgen.writeStringField("message", stageLinQDiscoveryMessage.getMessage());
        jgen.writeStringField("deviceID", stageLinQDiscoveryMessage.getDeviceID().toString());
        jgen.writeStringField("action", stageLinQDiscoveryMessage.getAction().toString());
        jgen.writeStringField("modelType", stageLinQDiscoveryMessage.getModelType().toString());
        jgen.writeStringField("modelName", stageLinQDiscoveryMessage.getModelCode().toString());
        jgen.writeStringField("version", stageLinQDiscoveryMessage.getSoftwareVersion());
        jgen.writeEndObject();
    }
}
