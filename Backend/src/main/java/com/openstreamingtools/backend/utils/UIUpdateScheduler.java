package com.openstreamingtools.backend.utils;

import com.openstreamingtools.backend.messaging.SongDataUpdateTask;
import lombok.extern.slf4j.Slf4j;

import static com.openstreamingtools.backend.config.OSTConfiguration.settings;

@Slf4j
public class UIUpdateScheduler implements Runnable{

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                SongDataUpdateTask task = Utils.taskQueue.take();
                Utils.timer.schedule(task,settings.getShowTrackDelay() * 1000L );
                Utils.addToScheduledTasks(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.debug("UI update schedule thread shut down.");
    }
}
