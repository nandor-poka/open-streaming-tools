package com.openstreamingtools.MainServer.api;

/**
 * THis class holds the runtime representation of the frontend settings
 * Actual values are read from and saved to resources/settings.json
 */
public class Settings {
    //defauls to avoid null valuse
    private int showTrackDelay = 0;
    private int volumeThreshold = 0;


    public Settings(int showTrackDelay, int volumeThreshold) {
        this.showTrackDelay = showTrackDelay;
        this.volumeThreshold = volumeThreshold;
    }

    public Settings() {
    }

    // Getters and setters
    public int getShowTrackDelay() {
        return showTrackDelay;
    }

    public void setShowTrackDelay(int showTrackDelay) {
        this.showTrackDelay = showTrackDelay;
    }

    public int getVolumeThreshold() {
        return volumeThreshold;
    }

    public void setVolumeThreshold(int volumeThreshold) {
        this.volumeThreshold = volumeThreshold;
    }
}

