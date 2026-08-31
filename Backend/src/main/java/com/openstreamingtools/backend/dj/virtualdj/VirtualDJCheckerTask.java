package com.openstreamingtools.backend.dj.virtualdj;

import com.openstreamingtools.backend.utils.ProcessChecker;

import java.util.TimerTask;

/**
 * Periodic timer task for checking Virtual DJ process status.
 * Checks if Virtual DJ application is running on the system.
 */
public class VirtualDJCheckerTask extends TimerTask {

    /**
     * Checks for Virtual DJ process and performs any necessary actions
     * if the process is detected.
     */
    @Override
    public void run() {
        if (ProcessChecker.checkProcessRunning("virtualdj")){

        }
    }
}
