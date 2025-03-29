package com.openstreamingtools.MainServer.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ProcessChecker {

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
