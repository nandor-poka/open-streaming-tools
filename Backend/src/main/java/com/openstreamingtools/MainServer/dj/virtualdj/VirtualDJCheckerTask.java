package com.openstreamingtools.MainServer.dj.virtualdj;

import com.openstreamingtools.MainServer.messaging.MessageSender;
import com.openstreamingtools.MainServer.utils.ProcessChecker;

import java.util.TimerTask;

public class VirtualDJCheckerTask extends TimerTask {
    @Override
    public void run() {
        if (ProcessChecker.checkProcessRunning("virtualdj")){

        }
    }
}
