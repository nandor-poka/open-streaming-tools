package com.openstreamingtools.MainServer.dj.virtualdj;

import java.util.Timer;

public class VirtualDJChecker {

    private static Timer vdjTimer;

    public static void checkForVDJ(){
        vdjTimer = new Timer();
        vdjTimer.scheduleAtFixedRate(new VirtualDJCheckerTask(), 0, 5000);
    }

    public static void stopChecking(){
        vdjTimer.purge();
        vdjTimer.cancel();
    }



}
