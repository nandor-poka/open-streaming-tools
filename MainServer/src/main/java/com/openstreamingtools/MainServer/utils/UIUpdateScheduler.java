package com.openstreamingtools.MainServer.utils;

import static com.openstreamingtools.MainServer.config.OSTConfiguration.settings;

public class UIUpdateScheduler implements Runnable{

    @Override
    public void run() {
        try {
            Utils.timer.schedule(Utils.taskQueue.take(),settings.getShowTrackDelay() * 1000L );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
