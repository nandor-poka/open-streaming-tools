package com.openstreamingtools.backend.dj.virtualdj;

import java.util.Timer;

/**
 * Utility class for monitoring and detecting Virtual DJ application availability.
 * Provides methods to start and stop periodic checks for VDJ presence on the system.
 * Uses a Timer to execute {@link VirtualDJCheckerTask} at regular intervals.
 */
public class VirtualDJChecker {

    private static Timer vdjTimer;

    /**
     * Starts periodic checks for Virtual DJ availability.
     * Executes a VirtualDJCheckerTask every 5 seconds starting immediately.
     */
    public static void checkForVDJ(){
        vdjTimer = new Timer();
        vdjTimer.scheduleAtFixedRate(new VirtualDJCheckerTask(), 0, 5000);
    }

    /**
     * Stops all Virtual DJ checks by canceling and purging the timer.
     */
    public static void stopChecking(){
        vdjTimer.purge();
        vdjTimer.cancel();
    }



}
