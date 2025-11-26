package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.messages.stagelinqmessages.BeatInfoMessage;

import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BeatInfoMessageSerializer  implements Serializer<byte[]>, Deserializer<BeatInfoMessage> {
    @Override
    public BeatInfoMessage deserialize(InputStream inputStream) throws IOException {
        return null;
    }

    @Override
    public void serialize(byte[] object, OutputStream outputStream) throws IOException {

    }
}
