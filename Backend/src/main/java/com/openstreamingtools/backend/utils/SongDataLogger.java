package com.openstreamingtools.backend.utils;

import com.openstreamingtools.backend.messages.frontend.SongData;
import com.openstreamingtools.backend.services.stagelinq.StateMapService;
import com.openstreamingtools.backend.twitch.TwitchUtils;
import com.openstreamingtools.backend.twitch.UserType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.openstreamingtools.backend.config.OSTConfiguration.settings;

/**
 * Background runnable that logs played tracks to a text file and announces them in Twitch chat.
 * Consumes song data from a queue, formats it with timestamps, and writes to a log file
 * while also sending track announcements to the bot's Twitch chat channel.
 */
@Slf4j
public class SongDataLogger implements Runnable{
    private static final  String userDirectory = Paths.get("")
            .toAbsolutePath()
            .toString();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
    private static final File youtubeSongLog = new File(userDirectory + "/youtubeTracklist"+ LocalDateTime.now().format(dateFormatter) +".txt");
    private static int counter=0;

    /**
     * Logs a list of song data entries to the log file and announces in Twitch chat.
     * Formats each entry with an incrementing track number and timestamp, then writes to file.
     * Sends a chat message to the bot's channel announcing the played tracks.
     *
     * @param songDataList list of song data entries to log
     */
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
                TwitchUtils.sendToChat("Track "+ trackNumsAsString + ": "+songsToLog, UserType.BOT);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Continuously processes song data from the log queue.
     * Groups song data entries together, logs them, and sends announcements.
     * Runs until the thread is interrupted.
     */
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
