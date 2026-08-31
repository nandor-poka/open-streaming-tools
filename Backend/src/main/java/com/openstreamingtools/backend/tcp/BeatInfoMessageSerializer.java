package com.openstreamingtools.backend.tcp;

import com.openstreamingtools.backend.messages.stagelinqmessages.BeatData;
import com.openstreamingtools.backend.messages.stagelinqmessages.BeatInfoMessage;

import com.openstreamingtools.backend.utils.Utils;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BeatInfoMessageSerializer  implements Serializer<byte[]>, Deserializer<BeatInfoMessage> {
    @Override
    public BeatInfoMessage deserialize(InputStream inputStream) throws IOException {
        // BeatInfo messages are at least 72 bytes
        // Format: id (4) + clock (8) + deckCount (4) + deckData (deckCount * 24) + samples (deckCount * 8)
        try{
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int id = Utils.convertBytesToInt(bis.readNBytes(4));
            long clock = Utils.convertBytesToLong(bis.readNBytes(8));
            int deckCount = Utils.convertBytesToInt(bis.readNBytes(4));
            BeatData[] decks = new BeatData[deckCount];
            for (int i = 0; i < deckCount; i++) {
                BeatData beatData = new BeatData();
                beatData.setBeat(Utils.convertBytesToInt(bis.readNBytes(8)));
                beatData.setTotalBeats(Utils.convertBytesToInt(bis.readNBytes(8)));
                beatData.setBPM(Utils.convertBytesToInt(bis.readNBytes(8)));
                decks[i] = beatData;
            }
            byte[] sampleBytes = new byte[8];
            for (int i = 0; i < deckCount; i++) {
                if (bis.read(sampleBytes)<8){
                    break;
                }
                decks[i].setSamples(Utils.convertBytesToLong(sampleBytes));
            }
            return new BeatInfoMessage(id, clock, deckCount, decks);
         } catch (Exception e){
            e.printStackTrace();
        }
        return new BeatInfoMessage();
    }
    @Override
    public void serialize(byte[] message, OutputStream outputStream) throws IOException {
        outputStream.write(message);
        outputStream.flush();
    }
}
