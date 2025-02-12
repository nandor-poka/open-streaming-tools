package com.openstreamingtools.MainServer.config;

import com.openstreamingtools.MainServer.dj.stagelinq.ActingAs;

public class Configuration {
    public static final String NAME = "Open Streaming Tools";
    public static final String VERSION = "0.0.1";
    public static final String SOURCE = "OST";
    public static final ActingAs STAGELINQ_ACTING_AS = ActingAs.LISTEN;
    public static final int STAGELINQ_BROADCAST_PORT = 51337;
    public static final String STAGELINQ_BORADCAST_IP = "192.168.178.255";
    public static final String WEBSOCKET_CONNECTION_PATH = "/websocket";
    public static final String WEBSOCKET_DATA_PATH = "/websocketData";
    private static boolean frontEndRunning = false;

    public static boolean isFrontEndRunning() {
        return frontEndRunning;
    }

    public static void setFrontEndRunning(boolean frontEndRunning) {
        Configuration.frontEndRunning = frontEndRunning;
    }
}
