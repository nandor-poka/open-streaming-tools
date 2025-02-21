package com.openstreamingtools.MainServer.api;

public class Settings {
    private int showTrackDelay = 0;
    private int volumeThreshold = 0;

    public Settings(int showTrackDelay, int volumeThreshold) {
        this.showTrackDelay = showTrackDelay;
        this.volumeThreshold = volumeThreshold;
    }

    public Settings() {
    }

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

