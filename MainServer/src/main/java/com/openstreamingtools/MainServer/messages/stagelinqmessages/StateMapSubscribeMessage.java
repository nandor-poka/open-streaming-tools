package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.dj.stagelinq.State;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class StateMapSubscribeMessage {

    private State state;
    private int paddingMarker = 0;

    public StateMapSubscribeMessage(State state) {
        this.state = state;
    }

    public byte[] getBytes(){
        Vector<Byte> buffer = new Vector<>();

        for (byte b: StateMapService.MAGIC_MARKER.getBytes(StandardCharsets.UTF_8)){
            buffer.add(b);
        }
        for (byte b: Utils.convertIntegerToByteArray(StateMapService.MAGIC_MARKER_INTERVAL)){
            buffer.add(b);
        }

        for (byte b : Utils.getNetworkBytesWithLenghForString(state.getStateValue(),StandardCharsets.UTF_16BE)){
            buffer.add(b);
        }

        for(byte b: Utils.convertIntegerToByteArray(paddingMarker)){
            buffer.add(b);
        }


        byte[] bytes = new byte[buffer.size()+4];
        byte[] msgLengthAsBytes = Utils.convertIntegerToByteArray(buffer.size());
        for (int i =0;i< 4;i++){
            bytes[i] = msgLengthAsBytes[i];
        }
        for (int i =4;i< bytes.length;i++){
            bytes[i] = buffer.get(i-4);
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "StateMapSubscribeMessage{" +
                "state=" + state.getStateValue() +
                '}';
    }
}
