package com.openstreamingtools.MainServer.config;

import com.openstreamingtools.MainServer.api.Settings;
import com.openstreamingtools.MainServer.dj.stagelinq.ActingAs;
import com.openstreamingtools.MainServer.utils.Utils;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

@Component
@Slf4j
public class OSTConfiguration {

    // Public constatncs
    public static final String NAME = "Open Streaming Tools";
    public static final String VERSION = "0.0.1";
    public static final String SOURCE = "OST";
    public static final ActingAs STAGELINQ_ACTING_AS = ActingAs.LISTEN;
    public static final int STAGELINQ_BROADCAST_PORT = 51337;
    public static final String STAGELINQ_BORADCAST_IP = "192.168.178.255";
    public static final String WEBSOCKET_CONNECTION_PATH = "/api/websocket";
    public static final String WEBSOCKET_DATA_PATH = "/api/websocketData";
    public static final String FRONTEND_ORIGN = "http://localhost:5173";
    public static final String FRONTEND_JAR_ORIGN = "http://localhost:8080";
    public static final int DIRECTORY_SERVICE_PORT = 60000;
    public static final int STATEMAP_SERVICE_PORT = 60001;
    public static final String SETTINGS_DIR_PATH = "../settings";
    public static final String SETTINGS_FILE_PATH = "../settings/settings.json";
    @Getter
    private static String TWITCH_CLIEND_ID ="";
    @Getter
    private static String TWITCH_CLIENT_SECRET = "";

    public static Settings settings;
    @Getter
    private static boolean frontEndRunning = false;
    @Setter
    private static File settingsFile;

     public static void init() {
        log.debug("reading settings from: "+ settingsFile.getAbsolutePath());
        try {

            settings = Utils.objectMapper.readValue(settingsFile , Settings.class);
            initTwitchClientData();
            Utils.UIUpdateSchedulerThread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFrontEndRunning(boolean frontEndRunning) {
        OSTConfiguration.frontEndRunning = frontEndRunning;
    }

    public static void saveSettings() {
        try {
            objectMapper.writeValue(settingsFile, settings);
            log.debug(objectMapper.writeValueAsString(settings));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initTwitchClientData(){
         if (settings.getClientIdFilePath() == null || settings.getClientSecretFilePath() == null){
             log.debug("File paths for twitch client credentials - ID and client secret - are empty.");
             return;
         }
         File clientIDFile = new File(settings.getClientIdFilePath());
         if (!clientIDFile.exists()){
             log.debug("Twitch client ID file at {} does not exist. Please check settings.", clientIDFile.getAbsolutePath());
         } else {
             try {
                 BufferedReader reader =  new BufferedReader(new FileReader(clientIDFile));
                 TWITCH_CLIEND_ID = reader.readLine();
             } catch (FileNotFoundException e) {
                 log.error("Client ID file at {} could not be read.", clientIDFile.getAbsolutePath());
                 return;
             } catch (IOException e) {
                 log.error("Error while reading client ID file.");
                 return;
             }
         }
        File clientSecretFile = new File(settings.getClientSecretFilePath());
        if (!clientSecretFile.exists()){
            log.debug("Twitch client secret file at: {} does not exist. Please check settings.", clientSecretFile.getAbsolutePath());
        } else {
            try {
                BufferedReader reader =  new BufferedReader(new FileReader(clientSecretFile));
                TWITCH_CLIENT_SECRET = reader.readLine();
            } catch (FileNotFoundException e) {
                log.error("Client secret file at {} could not be read.", clientSecretFile.getAbsolutePath());
                return;
            } catch (IOException e) {
                log.error("Error while reading client secret file.");
                return;
            }
        }
        settings.setTwitchStatus(true);
    }

    @PreDestroy
    public void shutdown(){
         Utils.taskQueue.clear();
         Utils.timer.purge();
         Utils.timer.cancel();
         Utils.UIUpdateSchedulerThread.interrupt();
        try {
            Utils.UIUpdateSchedulerThread.join(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
