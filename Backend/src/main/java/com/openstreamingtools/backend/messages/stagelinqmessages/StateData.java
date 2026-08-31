package com.openstreamingtools.backend.messages.stagelinqmessages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.dj.stagelinq.MixerState;
import com.openstreamingtools.backend.dj.stagelinq.PlayerState;
import com.openstreamingtools.backend.dj.stagelinq.SimpleState;
import com.openstreamingtools.backend.dj.stagelinq.State;
import com.openstreamingtools.backend.messages.frontend.ChannelVolumeData;
import com.openstreamingtools.backend.messaging.MessageSender;
import com.openstreamingtools.backend.services.stagelinq.StateMapService;
import com.openstreamingtools.backend.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Getter
@Setter
@Slf4j
public class StateData {
    boolean valid = false;
    UUID deviceId; // the uuid of the unit that sent the message
    State state; // name of the state;
    int interval; // the interval associated with the state
    String jsonString; // the json message associated with the state
    String value;
    int deckNum;

    public StateData() {
        valid = false;
    }
    public StateData(State state, String jsonString, int deckNum) {
        this.state = state;
        this.jsonString = jsonString;
        this.deckNum = deckNum;
        valid = true;
    }

    public static StateData parseStateData(byte[] messageBytes){
        try{
            // Check if the message starts with the magic marker
            if (!new String(Arrays.copyOfRange(messageBytes, 0, 4),StandardCharsets.UTF_8)
                .equals(StateMapService.MAGIC_MARKER) ){
                log.warn("Message does not start with magic marker. Ignoring message.");
                    return new StateData();
                }

            int dataType = Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 4,8));
            if (! (Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 4,8) )==StateMapService.MAGIC_MARKER_JSON)){
                log.warn("Message is doesn't have JSON Magic Marker. Ignoring message.");
                return new StateData();
            }
            int nameLength = Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 8,12) );
            String stateDataName = new String(Arrays.copyOfRange(messageBytes, 12, 12+nameLength), StandardCharsets.UTF_16BE);
            String jsonString = new String(Arrays.copyOfRange(messageBytes, 12+nameLength+4, messageBytes.length),StandardCharsets.UTF_16BE);
            State state;
            int decknum = 0; // indicates non deck related state.

            state = PlayerState.getByName(stateDataName);
            if (state == null){
                state = MixerState.getByName(stateDataName);
            }
            if (state.getStateName().contains("/Engine/Deck")){
                decknum = Integer.parseInt(
                        state.getStateName().split("/Engine/Deck")[1].split("/")[0]);

            }
            return new StateData(state, jsonString, decknum);
        } catch (Exception e) {
            log.error("Could not parse state data: {}",e.getMessage());
        }
        return new StateData();
    }

    @Override
    public String toString() {
        return "StateData{" +
                "deviceId=" + deviceId +
                ", state=" + state +
                ", interval=" + interval +
                ", jsonString='" + jsonString + '\'' +
                ", value='" + value + '\'' +
                ", deckNum=" + deckNum +
                '}';
    }
}