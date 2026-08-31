package com.openstreamingtools.backend.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility for checking if processes are running on Windows systems.
 * Uses the Windows `tasklist` command to verify process existence.
 */
@Slf4j
public class ProcessChecker {

    /**
     * Checks if a process with the given name is currently running.
     * Uses Windows tasklist command to query running processes.
     *
     * @param processName the name of the process to check for
     * @return true if process is found, false otherwise
     */
    public static boolean checkProcessRunning(String processName){
        try {
            Process cmdLine = new ProcessBuilder("tasklist | find /i \""+processName+"\"").start();
            String output;
            if ((output = new BufferedReader(new InputStreamReader(cmdLine.getInputStream())).readLine()) != null ) {
                String foundProcess = output.split("\t")[0];
                if (foundProcess.contains(processName)){
                return false;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
