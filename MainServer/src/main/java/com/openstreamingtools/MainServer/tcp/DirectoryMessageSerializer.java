package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.messages.stagelinqmessages.DirectoryMessage;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.Deserializer;

import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static com.openstreamingtools.MainServer.utils.Utils.putIntegerToByteArray;

public class DirectoryMessageSerializer implements Serializer<byte[]>, Deserializer<byte[]>  {


    private static final Logger logger = LoggerFactory.getLogger(DirectoryMessageSerializer.class);

    @NonNull
    @Override
    public byte[] deserialize(InputStream inputStream) throws IOException {
        byte[] messageAsBytes = new byte[44];
        if (inputStream.available() > 0) {
            int messageId = DirectoryMessage.parseMessageId(inputStream.readNBytes(4));
             switch (messageId){
                 case DirectoryService.SERVICE_ANNOUNCEMENT:
                     messageAsBytes = new byte[44];
                     putIntegerToByteArray( DirectoryService.SERVICE_ANNOUNCEMENT, messageAsBytes);
                     inputStream.readNBytes(messageAsBytes, 4, 22);
                     break;
                 case DirectoryService.SERVICE_REQUEST:
                     messageAsBytes = new byte[20];
                     putIntegerToByteArray( DirectoryService.SERVICE_REQUEST, messageAsBytes);
                     inputStream.readNBytes(messageAsBytes, 4, 16);
                     break;
                 case DirectoryService.TIMESTAMP:
                     messageAsBytes = new byte[44];
                     putIntegerToByteArray( DirectoryService.TIMESTAMP, messageAsBytes);
                     inputStream.readNBytes(messageAsBytes, 4, 40);
                     break;
             }

        }

        //logger.debug("Deserialized message {}", Arrays.toString(messageAsBytes));
        return messageAsBytes;
    }

    @Override
    public void serialize(@NonNull byte[] message, OutputStream outputStream) throws IOException {
        //logger.info("Serializing {}", Arrays.toString(message));
        outputStream.write(message);
        outputStream.flush();
    }


}
