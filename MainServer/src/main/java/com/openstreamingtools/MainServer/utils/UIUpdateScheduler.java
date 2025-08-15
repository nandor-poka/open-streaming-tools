package com.openstreamingtools.MainServer.utils;

import lombok.extern.slf4j.Slf4j;

import static com.openstreamingtools.MainServer.config.OSTConfiguration.settings;

@Slf4j
public class UIUpdateScheduler implements Runnable{

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                Utils.timer.schedule(Utils.taskQueue.take(),settings.getShowTrackDelay() * 1000L );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.debug("Ui update schedule thread shut down.");
    }
}
