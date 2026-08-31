package com.openstreamingtools.backend.utils;

import com.openstreamingtools.backend.messaging.SongDataUpdateTask;
import lombok.extern.slf4j.Slf4j;

import static com.openstreamingtools.backend.config.OSTConfiguration.settings;

/**
 * Background runnable that schedules UI update tasks with configured delays.
 * Continuously consumes song data update tasks from a queue and schedules them
 * for execution after a user-configurable display delay.
 */
@Slf4j
public class UIUpdateScheduler implements Runnable{

    /**
     * Continuously processes song data update tasks from the task queue.
     * Schedules each task for execution after the configured show track delay.
     * Runs until the thread is interrupted.
     */
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
