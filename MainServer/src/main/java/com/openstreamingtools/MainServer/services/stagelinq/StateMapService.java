package com.openstreamingtools.MainServer.services.stagelinq;

import com.openstreamingtools.MainServer.dj.stagelinq.SimpleState;
import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.Service;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceType;
import com.openstreamingtools.MainServer.messaging.SongDataUpdateTask;
import com.openstreamingtools.MainServer.utils.Utils;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import static com.openstreamingtools.MainServer.config.OSTConfiguration.settings;

public class StateMapService extends Service {

    public static final String MAGIC_MARKER = "smaa";
    public static final int MAGIC_MARKER_INTERVAL = 0x000007d2;
    public static final int MAGIC_MARKER_JSON = 0x00000000;
    public static final Map<Integer, Map<SimpleState,Object>> deckStates = new HashMap<>();
    public static final Map<Integer, Integer> keyIndexToKeyMapping = new HashMap<>();
    private boolean isDeviceService = false;
    private static boolean firstTrack = false;
    public static Instant firstTrackTime;

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
        deckStates.get(1).put(SimpleState.KEY, -1);
        deckStates.get(2).put(SimpleState.KEY, -1);
        deckStates.get(3).put(SimpleState.KEY, -1);
        deckStates.get(4).put(SimpleState.KEY, -1);
        keyIndexToKeyMapping.put(21, 1);
        keyIndexToKeyMapping.put(7, 2);
        keyIndexToKeyMapping.put(16, 3);
        keyIndexToKeyMapping.put(2, 4);
        keyIndexToKeyMapping.put(23, 5);
        keyIndexToKeyMapping.put(9, 6);
        keyIndexToKeyMapping.put(0, 7);
        keyIndexToKeyMapping.put(4, 8);
        keyIndexToKeyMapping.put(13, 9);
        keyIndexToKeyMapping.put(11, 10);
        keyIndexToKeyMapping.put(20, 11);
        keyIndexToKeyMapping.put(6, 12);
        keyIndexToKeyMapping.put(15, 13);
        keyIndexToKeyMapping.put(1, 14);
        keyIndexToKeyMapping.put(22, 15);
        keyIndexToKeyMapping.put(8, 16);
        keyIndexToKeyMapping.put(17, 17);
        keyIndexToKeyMapping.put(3, 18);
        keyIndexToKeyMapping.put(12, 19);
        keyIndexToKeyMapping.put(10, 20);
        keyIndexToKeyMapping.put(19, 21);
        keyIndexToKeyMapping.put(5, 22);
        keyIndexToKeyMapping.put(14, 23);
        keyIndexToKeyMapping.put(18, 24);
    }

    public static void updateDeckState(int deck, SimpleState state, Object value){
        if(!firstTrack){
            firstTrack = true;
            firstTrackTime = Instant.now();
        }
        deckStates.get(deck).put(state, value);
        SongDataUpdateTask updateTask = new SongDataUpdateTask(new SongData(
                deck, (String) deckStates.get(deck).get(SimpleState.SONG_NAME),
                (String) deckStates.get(deck).get(SimpleState.ARTIST_NAME),
                (Integer) deckStates.get(deck).get(SimpleState.KEY)));
        SongDataUpdateTask emptySongDataTask = new SongDataUpdateTask(new SongData(
                deck, " ",
                " ", -1));
        if ((int)deckStates.get(deck).get(SimpleState.VOLUME) >= settings.getVolumeThreshold()){
       //     && (long)deckStates.get(deck).get(SimpleState.LAST_UPDATE) < System.currentTimeMillis()-5000){
          //  if(!((boolean) deckStates.get(deck).get(SimpleState.IS_SHOWING))) {
          //      StateMapService.deckStates.get(deck).put(SimpleState.IS_SHOWING, true);

            if (!Utils.taskQueue.contains(updateTask)){
                Utils.taskQueue.offer(updateTask);
            }
            Optional<SongDataUpdateTask> task = Utils.taskQueue.stream().filter(e -> e.equals(emptySongDataTask)).findFirst();
            task.ifPresent(TimerTask::cancel);
            Utils.taskQueue.remove(emptySongDataTask);
            //}
        }
        if ((int)deckStates.get(deck).get(SimpleState.VOLUME) == 0){
      //  && (long)deckStates.get(deck).get(SimpleState.LAST_UPDATE) < System.currentTimeMillis()-5000){
           // if( (boolean)deckStates.get(deck).get(SimpleState.IS_SHOWING)){
             //   StateMapService.deckStates.get(deck).put(SimpleState.IS_SHOWING, false);

            if (!Utils.taskQueue.contains(emptySongDataTask)){
                Utils.taskQueue.offer(emptySongDataTask);
            }
            Optional<SongDataUpdateTask> task = Utils.taskQueue.stream().filter(e -> e.equals(updateTask)).findFirst();
            task.ifPresent(TimerTask::cancel);
            Utils.taskQueue.remove(updateTask);

            //}
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
