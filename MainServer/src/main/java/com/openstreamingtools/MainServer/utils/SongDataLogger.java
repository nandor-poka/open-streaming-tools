package com.openstreamingtools.MainServer.utils;

import com.openstreamingtools.MainServer.messages.frontend.SongData;
import com.openstreamingtools.MainServer.services.stagelinq.StateMapService;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


public class SongDataLogger {
    private static final  String userDirectory = Paths.get("")
            .toAbsolutePath()
            .toString();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
    private static final DateTimeFormatter chapterTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final File detailedSongLog = new File(userDirectory + "/songData_"+ LocalDateTime.now().format(dateFormatter) +".txt");
    private static final File youtubeSongLog = new File(userDirectory + "/youtubeTracklist"+ LocalDateTime.now().format(dateFormatter) +".txt");
    private static int counter=0;
   private static final Duration deltaTime = Duration.ZERO;
    public static void logSongData(SongData songData) {
        try {
            if (!detailedSongLog.exists()) {
                detailedSongLog.createNewFile();
            }
            if (!detailedSongLog.exists()) {
                detailedSongLog.createNewFile();
            }
            if (!songData.getArtistName().equals(" ") || !songData.getTrackTitle().equals(" ")){
                FileWriter songDataFileFriter = new FileWriter(detailedSongLog, true);
                long durationSeconds = Duration.between(Instant.now(), StateMapService.firstTrackTime).getSeconds();

                songDataFileFriter.write(  String.format("%d:%02d:%02d", durationSeconds / 3600, (durationSeconds % 3600) / 60, (durationSeconds % 60))+" on deck "
                        +songData.getDeckNumber() + " " + songData.getTrackTitle() + " - " + songData.getArtistName() + "\n");
                songDataFileFriter.close();
                FileWriter youtubeLogFileWriter = new FileWriter(youtubeSongLog, true);
                youtubeLogFileWriter.write( String.format("%d:%02d:%02d", durationSeconds / 3600, (durationSeconds % 3600) / 60, (durationSeconds % 60))+" " + songData.getTrackTitle() + " - " + songData.getArtistName() + "\n");
                youtubeLogFileWriter.close();
                TwitchUtils.sendToChat("Track "+ ++counter + ": "+songData.getTrackTitle() + " - " + songData.getArtistName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
