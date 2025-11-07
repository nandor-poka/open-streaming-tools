package com.openstreamingtools.MainServer.utils;

import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import jdk.jshell.execution.Util;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.Utilities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.openstreamingtools.MainServer.config.OSTConfiguration.settings;

@Slf4j
public class SongDataLogger implements Runnable{
    private static final  String userDirectory = Paths.get("")
            .toAbsolutePath()
            .toString();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
    private static final File youtubeSongLog = new File(userDirectory + "/youtubeTracklist"+ LocalDateTime.now().format(dateFormatter) +".txt");
    private static int counter=0;
    public static void logSongData(List<SongData> songDataList) {
        try {
            log.debug( youtubeSongLog.createNewFile() ? youtubeSongLog.getAbsolutePath()+" created." : "");
            String trackNumsAsString = "";
            String songsToLog = "";
            for (SongData songData : songDataList){
                if (!songData.getArtistName().equals(" ") || !songData.getTrackTitle().equals(" ")){
                    trackNumsAsString = trackNumsAsString.isEmpty() ? String.valueOf(++counter) : trackNumsAsString + " / " + (++counter) ;
                    songsToLog = songsToLog.isEmpty() ? songData.getArtistName() + " - " + songData.getTrackTitle():
                            songsToLog + " / " + songData.getArtistName() + " - " + songData.getTrackTitle();
                }
            }
            if (!trackNumsAsString.isEmpty()){
                long durationSeconds = Duration.between(StateMapService.firstTrackTime, Instant.now()).getSeconds()-settings.getShowTrackDelay();
                FileWriter youtubeLogFileWriter = new FileWriter(youtubeSongLog, true);
                youtubeLogFileWriter.write( String.format("%d:%02d:%02d", durationSeconds / 3600,
                        (durationSeconds % 3600) / 60, (durationSeconds % 60))
                        +" " +songsToLog+ "\n");
                youtubeLogFileWriter.close();
                TwitchUtils.sendToChat("Track "+ trackNumsAsString + ": "+songsToLog);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        List<SongData>  songDataToLog = new ArrayList<>();
        while (!Thread.currentThread().isInterrupted()){
            songDataToLog.clear();
            try {
                SongData sd = Utils.logDataQueue.take();
                SongData sd2 = Utils.logDataQueue.poll(500, TimeUnit.MILLISECONDS);
                songDataToLog.add(sd);
                if (sd2 != null){
                    songDataToLog.add(sd2);
                }
                log.debug(songDataToLog.toString());
                logSongData(songDataToLog);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
        log.debug("Song data logger thread exited.");

    }
}
