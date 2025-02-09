package com.openstreamingtools.MainServer.tcp;

import com.openstreamingtools.MainServer.messages.stagelinqmessages.DirectoryMessage;
import com.openstreamingtools.MainServer.services.stagelinq.DirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.Deserializer;

import org.springframework.core.serializer.Serializer;
import org.springframework.lang.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static com.openstreamingtools.MainServer.utils.Utils.putIntegerToByteArray;

public class DirectoryMessageSerializer implements Serializer<byte[]>, Deserializer<DirectoryMessage>  {


    private static final Logger logger = LoggerFactory.getLogger(DirectoryMessageSerializer.class);

    @NonNull
    @Override
    public DirectoryMessage deserialize(InputStream inputStream) throws IOException {
        byte[] messageAsBytes;
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        //if (bis.available() > 0) {
            int messageId = DirectoryMessage.parseMessageId(bis.readNBytes(4));
             switch (messageId){
                 case DirectoryService.SERVICE_ANNOUNCEMENT:
                     messageAsBytes = new byte[26];
                     putIntegerToByteArray( DirectoryService.SERVICE_ANNOUNCEMENT, messageAsBytes);
                     bis.readNBytes(messageAsBytes, 4, 22);
                     return DirectoryMessage.parseMessage(messageAsBytes);
                 case DirectoryService.SERVICE_REQUEST:
                     messageAsBytes = new byte[20];
                     putIntegerToByteArray( DirectoryService.SERVICE_REQUEST, messageAsBytes);
                     bis.readNBytes(messageAsBytes, 4, 16);
                     return DirectoryMessage.parseMessage(messageAsBytes);
                 case DirectoryService.TIMESTAMP:
                     messageAsBytes = new byte[44];
                     putIntegerToByteArray( DirectoryService.TIMESTAMP, messageAsBytes);
                     bis.readNBytes(messageAsBytes, 4, 40);
                     return DirectoryMessage.parseMessage(messageAsBytes);
                 default:
                     return new DirectoryMessage(-1);
             }

       // }

        //logger.debug("No bytes were available.");
        //return new DirectoryMessage(-1);
    }

    @Override
    public void serialize(@NonNull byte[] message, OutputStream outputStream) throws IOException {
        //logger.info("Serializing {}", Arrays.toString(message));
        outputStream.write(message);
        outputStream.flush();
    }


}
