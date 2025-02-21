package com.openstreamingtools.MainServer.utils;

import com.openstreamingtools.MainServer.messages.frontend.SongData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SongDataLogger {
    private static final  String userDirectory = Paths.get("")
            .toAbsolutePath()
            .toString();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HHmmss");

    private static final File file = new File(userDirectory + "/songData_"+ LocalDateTime.now().format(dateFormatter) +".txt");

    public static void logSongData(SongData songData) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(songData.getDeckNumber() + " " + songData.getTrackTitle() + " " + songData.getArtistName() + "\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
