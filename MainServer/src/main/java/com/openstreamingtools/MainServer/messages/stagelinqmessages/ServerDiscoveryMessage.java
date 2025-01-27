package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.config.Configuration;
import com.openstreamingtools.MainServer.dj.stagelinq.ActingAs;
import com.openstreamingtools.MainServer.dj.stagelinq.StageLinQAction;
import com.openstreamingtools.MainServer.dj.UnitUtils;

import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class ServerDiscoveryMessage {
    String name = Configuration.NAME;
    String version = Configuration.VERSION;
    String source = Configuration.SOURCE;
    byte[] actingAs = Configuration.STAGELINQ_ACTING_AS.getValue();
    private Byte[] messageAsBytes = null;



    public byte[] getMessageAsBytes() {
       if (null == messageAsBytes){
            generateMessageBytes();
        }
        byte[] convertedArray = new byte[messageAsBytes.length];
        for (int i=0; i<messageAsBytes.length; i++){
            convertedArray[i] = (byte) messageAsBytes[i];
        }
        return convertedArray;
    }

   private void generateMessageBytes(){
        Vector<Byte> buffer = new Vector<Byte>();
        // opening tag for StageLinQ messages
        for (byte b : UnitUtils.STAGELINQ_MESSAGE_START.getBytes()){
            buffer.add(b);
        }
        // Listening service id
        for (byte b: actingAs){
            buffer.add(b);
        }
        int sourceLength = source.length()*2;
        buffer.add((byte) ((byte)sourceLength >>24 & 0xFF));
        buffer.add((byte) ((byte)sourceLength >>16 & 0xFF));
        buffer.add((byte) ((byte)sourceLength >>18 & 0xFF));
        buffer.add((byte) ((byte)sourceLength & 0xFF));

        // source string as UTF16
        for (byte b: source.getBytes(StandardCharsets.UTF_16BE)){
            buffer.add(b);
        }
        int actionLength = StageLinQAction.HOWDY.getAction().length()*2;
        buffer.add((byte) ((byte)actionLength >>24 & 0xFF));
        buffer.add((byte) ((byte)actionLength >>16 & 0xFF));
        buffer.add((byte) ((byte)actionLength >>18 & 0xFF));
        buffer.add((byte) ((byte)actionLength & 0xFF));
        // action string as UTF16, action is always login
        for (byte b: StageLinQAction.HOWDY.getAction().getBytes(StandardCharsets.UTF_16BE)){
            buffer.add(b);
        }
        int nameLength = name.length()*2;
        buffer.add((byte) ((byte)nameLength >>24 & 0xFF));
        buffer.add((byte) ((byte)nameLength >>16 & 0xFF));
        buffer.add((byte) ((byte)nameLength >>18 & 0xFF));
        buffer.add((byte) ((byte)nameLength & 0xFF));

        // name string as UTF16
        for (byte b: name.getBytes(StandardCharsets.UTF_16BE)){
            buffer.add(b);
        }
        int versionLength = version.length()*2;
        buffer.add((byte) ((byte)versionLength >>24 & 0xFF));
        buffer.add((byte) ((byte)versionLength >>16 & 0xFF));
        buffer.add((byte) ((byte)versionLength >>18 & 0xFF));
        buffer.add((byte) ((byte)versionLength & 0xFF));
        // version string as UTF16
        for (byte b: version.getBytes(StandardCharsets.UTF_16BE)){
            buffer.add(b);
        }

        // port 0 as 16 bit integer

        buffer.add((byte) ((byte) (60000 >> 8) & 0xFF));
        buffer.add((byte) (60000));

        messageAsBytes = new Byte[buffer.size()];
        buffer.toArray(messageAsBytes);
    }

}
