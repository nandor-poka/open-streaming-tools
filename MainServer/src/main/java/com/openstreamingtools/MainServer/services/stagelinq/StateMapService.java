package com.openstreamingtools.MainServer.services.stagelinq;

import com.openstreamingtools.MainServer.dj.stagelinq.SimpleState;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.Service;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceType;
import com.openstreamingtools.MainServer.messaging.SongDataUpdateTask;
import com.openstreamingtools.MainServer.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.openstreamingtools.MainServer.config.OSTConfiguration.settings;

public class StateMapService extends Service {

    public static final String MAGIC_MARKER = "smaa";
    public static final int MAGIC_MARKER_INTERVAL = 0x000007d2;
    public static final int MAGIC_MARKER_JSON = 0x00000000;
    public static final Map<Integer, Map<SimpleState,Object>> deckStates = new HashMap<>();
    private boolean isDeviceService = false;
    public StateMapService() {
        super();
        this.type= ServiceType.STATEMAP;
        initDeckStates();
    }

    private void initDeckStates() {
        deckStates.put(2, new HashMap<>());
        deckStates.put(3, new HashMap<>());
        deckStates.put(4, new HashMap<>());
        deckStates.put(1, new HashMap<>());
        deckStates.get(1).put(SimpleState.IS_SHOWING, false);
        deckStates.get(2).put(SimpleState.IS_SHOWING, false);
        deckStates.get(3).put(SimpleState.IS_SHOWING, false);
        deckStates.get(4).put(SimpleState.IS_SHOWING, false);
        deckStates.get(1).put(SimpleState.SONG_NAME, " ");
        deckStates.get(2).put(SimpleState.SONG_NAME, " ");
        deckStates.get(3).put(SimpleState.SONG_NAME, " ");
        deckStates.get(4).put(SimpleState.SONG_NAME, " ");
        deckStates.get(1).put(SimpleState.ARTIST_NAME, " ");
        deckStates.get(2).put(SimpleState.ARTIST_NAME, " ");
        deckStates.get(3).put(SimpleState.ARTIST_NAME, " ");
        deckStates.get(4).put(SimpleState.ARTIST_NAME, " ");
    }

    public static void updateDeckState(int deck, SimpleState state, Object value){
        deckStates.get(deck).put(state, value);

        if ((int)deckStates.get(deck).get(SimpleState.VOLUME) >= settings.getVolumeThreshold()){
       //     && (long)deckStates.get(deck).get(SimpleState.LAST_UPDATE) < System.currentTimeMillis()-5000){
            if(!((boolean) deckStates.get(deck).get(SimpleState.IS_SHOWING))) {
                StateMapService.deckStates.get(deck).put(SimpleState.IS_SHOWING, true);
                Utils.timer.schedule(new SongDataUpdateTask(new SongData(
                                deck, (String) deckStates.get(deck).get(SimpleState.SONG_NAME),
                                (String) deckStates.get(deck).get(SimpleState.ARTIST_NAME))),
                        settings.getShowTrackDelay() * 1000L);
            }
        }
        if ((int)deckStates.get(deck).get(SimpleState.VOLUME) == 0){
      //  && (long)deckStates.get(deck).get(SimpleState.LAST_UPDATE) < System.currentTimeMillis()-5000){
            if( (boolean)deckStates.get(deck).get(SimpleState.IS_SHOWING)){
                StateMapService.deckStates.get(deck).put(SimpleState.IS_SHOWING, false);
                Utils.timer.schedule(new SongDataUpdateTask(new SongData(
                        deck, " ",
                        " ")), 5000L);
            }
        }



    }
    public StateMapService(ServiceType serviceType, int port) {
        super();
        this.type = serviceType;
        this.unitPort = port;
        this.isDeviceService = true;
        initDeckStates();
    }

    public boolean isDeviceService() {
        return isDeviceService;
    }
}
