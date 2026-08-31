package com.openstreamingtools.backend.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openstreamingtools.backend.dj.stagelinq.MixerState;
import com.openstreamingtools.backend.dj.stagelinq.PlayerState;
import com.openstreamingtools.backend.dj.stagelinq.SimpleState;
import com.openstreamingtools.backend.dj.stagelinq.State;
import com.openstreamingtools.backend.messages.frontend.ChannelVolumeData;
import com.openstreamingtools.backend.messages.stagelinqmessages.*;
import com.openstreamingtools.backend.messaging.MessageSender;
import com.openstreamingtools.backend.services.stagelinq.DirectoryService;
import com.openstreamingtools.backend.services.stagelinq.StateMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Vector;

@Component
@MessageEndpoint
@Slf4j
public class StateMapMessageHandler {

    @ServiceActivator(inputChannel = "toStateMap")
    public byte[] handleStateMapMessage(Message<StateMapMessage< ?>> message) throws JsonProcessingException {
        StateMapMessage<?> stateMapMessage = message.getPayload();
        if (!DirectoryService.getUnitByIP((String) message.getHeaders().get(IpHeaders.IP_ADDRESS)).hasService(ServiceType.STATEMAP)) {
            if (stateMapMessage.getData() instanceof ServiceAnnouncement sa) {
                if (sa.hasService(ServiceType.STATEMAP)) {
                    Vector<Byte> buffer = new Vector<>();
                    for (State state : PlayerState.values()) {
                        log.debug("Subscribing to state {}", state);
                        for (byte b : new StateMapSubscribeMessage(state).getBytes()) {
                            buffer.add(b);
                        }

                    }
                    for (State state : MixerState.values()) {
                        log.debug("Subscribing to state {}", state);
                        for (byte b : new StateMapSubscribeMessage(state).getBytes()) {
                            buffer.add(b);
                        }
                    }
                    byte[] response = new byte[buffer.size()];
                    for (int i = 0; i < response.length; i++) {
                        response[i] = buffer.get(i);
                    }
                    DirectoryService.getUnit(sa.getDeviceId()).addService(
                            new StateMapService(ServiceType.STATEMAP, sa.getService(ServiceType.STATEMAP).getUnitPort()));
                    log.info("Subscribed to services.");
                    return response;

                }
            }

            if (stateMapMessage.getData() instanceof StateData[] states) {
                for (StateData stateData : states) {
                    if (!stateData.isValid()) {
                        log.warn("Received invalid state data. Ignoring.");
                        return new byte[0];
                    }
                    checkStateData(stateData);
                    }
            }
            log.debug("Received state map message {}.", stateMapMessage);
        }

        return new byte[0];
    }

    private void checkStateData(StateData stateData)  {
        try{
            State state = stateData.getState();
            // /Engine/Deck2/Track/ArtistName, type 0, jsonString: {"string":"Ekko & Sidetrack","type":8}
            if (state.equals(PlayerState.EngineDeck1TrackArtistName)
                    || state.equals(PlayerState.EngineDeck2TrackArtistName)
                    || state.equals(PlayerState.EngineDeck3TrackArtistName)
                    || state.equals(PlayerState.EngineDeck4TrackArtistName)) {
                //jsonString: {"string":"Ekko & Sidetrack","type":8}
                String[] artistNamePrep = stateData.getJsonString().split(":")[1].split(",")[0].split("\"");
                if (artistNamePrep.length>0){
                    StateMapService.deckStates.get(stateData.getDeckNum()).put(SimpleState.ARTIST_NAME, artistNamePrep[1]);
                }
            }
            if (state.equals(PlayerState.EngineDeck1TrackCurrentKeyIndex)
                    || state.equals(PlayerState.EngineDeck2TrackCurrentKeyIndex)
                    || state.equals(PlayerState.EngineDeck3TrackCurrentKeyIndex)
                    || state.equals(PlayerState.EngineDeck4TrackCurrentKeyIndex)){
                String keyIndex = stateData.getJsonString().split(":")[2].split("}")[0];
                int key = -1;
                if (keyIndex != null){
                    key = Integer.parseInt(keyIndex);
                    log.debug("Deck {}, current key: {}", stateData.getDeckNum(), key);
                }
                if (key > -1){
                    StateMapService.deckStates.get(stateData.getDeckNum()).put(SimpleState.KEY, StateMapService.keyIndexToKeyMapping.get(key));
                }else {
                    StateMapService.deckStates.get(stateData.getDeckNum()).put(SimpleState.KEY, key);
                }
            }
            //SateMap name /Engine/Deck2/Track/SongName, type 0, jsonString: {"string":"Synchronise","type":8}
            if (state.equals(PlayerState.EngineDeck1TrackSongName)
                    || state.equals(PlayerState.EngineDeck2TrackSongName)
                    || state.equals(PlayerState.EngineDeck3TrackSongName)
                    || state.equals(PlayerState.EngineDeck4TrackSongName)) {
                //jsonString: {"string":"Synchronise","type":8}
                String[] songNamePrep = stateData.getJsonString().split(":")[1].split(",")[0].split("\"");
                if (songNamePrep.length>0){
                    StateMapService.updateDeckState(stateData.getDeckNum(), SimpleState.SONG_NAME, songNamePrep[1]);
                }
            }
            if (state.equals(PlayerState.EngineDeck1ExternalMixerVolume)
                    || state.equals(PlayerState.EngineDeck2ExternalMixerVolume)
                    || state.equals(PlayerState.EngineDeck3ExternalMixerVolume)
                    || state.equals(PlayerState.EngineDeck4ExternalMixerVolume)) {
                //ExternalMixerVolume N{"type":0,"value":0.012926282361149788}
                int volume = Math.round(Float.parseFloat(
                        stateData.getJsonString().split("\"value\":")[1].split("}")[0])*100);
                log.debug("Deck {}, volume {}", stateData.getDeckNum(), volume);
                MessageSender.sendMessage(new ChannelVolumeData(stateData.getDeckNum(), volume));
                StateMapService.updateDeckState(stateData.getDeckNum(), SimpleState.VOLUME, volume);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
