package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import com.openstreamingtools.MainServer.dj.stagelinq.MixerState;
import com.openstreamingtools.MainServer.dj.stagelinq.PlayerState;
import com.openstreamingtools.MainServer.dj.stagelinq.State;
import com.openstreamingtools.MainServer.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class StateData {
    UUID deviceId; // the uuid of the unit that sent the message
    State state; // name of the state;
    int interval; // the interval associated with the state
    String jsonString; // the json message associated with the state
    String value;
    int deckNum;

    public StateData(State state, String jsonString, int deckNum) {
        this.state = state;
        this.jsonString = jsonString;
        this.deckNum = deckNum;
    }

    public static StateData parseStateData(byte[] messageBytes){
        int nameLength = Utils.convertBytesToInt(Arrays.copyOfRange(messageBytes, 8,12) );
        String stateDataName = new String(Arrays.copyOfRange(messageBytes, 12, 12+nameLength), StandardCharsets.UTF_16BE);
        String jsonString = new String(Arrays.copyOfRange(messageBytes, 12+nameLength+4, messageBytes.length),StandardCharsets.UTF_16BE);
        State state;
        int decknum =0;
        state = PlayerState.getByName(stateDataName);
        if (state == null){
            state = MixerState.getByName(stateDataName);
            return new StateData(state, jsonString, decknum);
        }
        if (state.equals(PlayerState.EngineDeck1TrackArtistName)
                || state.equals(PlayerState.EngineDeck2TrackArtistName)
                || state.equals(PlayerState.EngineDeck3TrackArtistName)
                || state.equals(PlayerState.EngineDeck4TrackArtistName)
                || state.equals(PlayerState.EngineDeck1TrackSongName)
                || state.equals(PlayerState.EngineDeck2TrackSongName)
                || state.equals(PlayerState.EngineDeck3TrackSongName)
                || state.equals(PlayerState.EngineDeck4TrackSongName)
                || state.equals(PlayerState.EngineDeck1ExternalMixerVolume)
                || state.equals(PlayerState.EngineDeck2ExternalMixerVolume)
                || state.equals(PlayerState.EngineDeck3ExternalMixerVolume)
                || state.equals(PlayerState.EngineDeck4ExternalMixerVolume)
                || state.equals(PlayerState.EngineDeck1TrackCurrentKeyIndex)
                || state.equals(PlayerState.EngineDeck2TrackCurrentKeyIndex)
                || state.equals(PlayerState.EngineDeck3TrackCurrentKeyIndex)
                || state.equals(PlayerState.EngineDeck4TrackCurrentKeyIndex)) {
            decknum = Integer.parseInt(
                    state.getStateName().split("/Engine/Deck")[1].split("/")[0]);
            return new StateData(state, jsonString, decknum);

        }
        return new StateData(state, jsonString, decknum);
    }

    public State getState() {
        return state;
    }

    public int getDeckNum() {
        return deckNum;
    }

    public String getJsonString() {
        return jsonString;
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