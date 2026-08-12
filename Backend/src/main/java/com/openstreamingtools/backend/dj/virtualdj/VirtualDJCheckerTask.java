package com.openstreamingtools.backend.dj.virtualdj;

import com.openstreamingtools.backend.utils.ProcessChecker;

import java.util.TimerTask;

public class VirtualDJCheckerTask extends TimerTask {
    @Override
    public void run() {
        if (ProcessChecker.checkProcessRunning("virtualdj")){

        }
    }
}
