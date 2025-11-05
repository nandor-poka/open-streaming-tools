package com.openstreamingtools.MainServer.utils;

import com.openstreamingtools.MainServer.messaging.SongDataUpdateTask;
import lombok.extern.slf4j.Slf4j;

import static com.openstreamingtools.MainServer.config.OSTConfiguration.settings;

@Slf4j
public class UIUpdateScheduler implements Runnable{

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                SongDataUpdateTask task = Utils.taskQueue.take();
                if (!Utils.taskQueue.isEmpty()){
                    SongDataUpdateTask nextTask = Utils.taskQueue.peek();
                    if (nextTask.getTimestamp() > (task.getTimestamp() - 2000L)) {
                        task.getSongData().add(nextTask.getSongData().getFirst());
                        nextTask.cancel();
                        Utils.taskQueue.remove();
                    }
                }

                Utils.timer.schedule(task,settings.getShowTrackDelay() * 1000L );
                Utils.addToScheduledTasks(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        log.debug("UI update schedule thread shut down.");
    }
}
