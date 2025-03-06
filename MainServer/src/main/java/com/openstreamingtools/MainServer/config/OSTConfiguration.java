package com.openstreamingtools.MainServer.config;

import com.openstreamingtools.MainServer.api.Settings;
import com.openstreamingtools.MainServer.dj.stagelinq.ActingAs;
import com.openstreamingtools.MainServer.utils.Utils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class OSTConfiguration {

    public static final String NAME = "Open Streaming Tools";
    public static final String VERSION = "0.0.1";
    public static final String SOURCE = "OST";
    public static final ActingAs STAGELINQ_ACTING_AS = ActingAs.LISTEN;
    public static final int STAGELINQ_BROADCAST_PORT = 51337;
    public static final String STAGELINQ_BORADCAST_IP = "192.168.178.255";
    public static final String WEBSOCKET_CONNECTION_PATH = "/websocket";
    public static final String WEBSOCKET_DATA_PATH = "/websocketData";
    public static final String FRONTEND_ORIGN = "http://localhost:5173";
    public static final String FRONTEND_JAR_ORIGN = "http://localhost:8080";
    public static final int DIRECTORY_SERVICE_PORT = 60000;
    public static final int STATEMAP_SERVICE_PORT = 60001;
    private static boolean frontEndRunning = false;
    public static final String SETTINGS_DIR_PATH = "../settings";
    public static final String SETTINGS_FILE_PATH = "../settings/settings.json";

    private static File settingsFile;
    public static Settings settings;

     public static void init() {
        try {
            settings = Utils.objectMapper.readValue(settingsFile , Settings.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isFrontEndRunning() {
        return frontEndRunning;
    }

    public static void setFrontEndRunning(boolean frontEndRunning) {
        OSTConfiguration.frontEndRunning = frontEndRunning;
    }

    public static File getSettingsFile() {
        return settingsFile;
    }

    public static void setSettingsFile(File file) {
        settingsFile = file;
    }
}
