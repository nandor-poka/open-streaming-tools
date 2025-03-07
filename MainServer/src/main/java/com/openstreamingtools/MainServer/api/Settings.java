package com.openstreamingtools.MainServer.api;

public class Settings {
    private int showTrackDelay = 5;
    private int volumeThreshold = 75;
    private int sdRed = 0;
    private int sdGreen = 128;
    private int sdBlue = 255;
    private int faderRed = 0;
    private int faderGreen = 255;
    private int faderBlue = 128;

    public Settings(int showTrackDelay, int volumeThreshold) {
        this.showTrackDelay = showTrackDelay;
        this.volumeThreshold = volumeThreshold;
    }

    public Settings() {
    }

    public int getFaderBlue() {
        return faderBlue;
    }

    public void setFaderBlue(int faderBlue) {
        this.faderBlue = faderBlue;
    }

    public int getFaderGreen() {
        return faderGreen;
    }

    public void setFaderGreen(int faderGreen) {
        this.faderGreen = faderGreen;
    }

    public int getFaderRed() {
        return faderRed;
    }

    public void setFaderRed(int faderRed) {
        this.faderRed = faderRed;
    }

    public int getSdRed() {
        return sdRed;
    }

    public void setSdRed(int sdRed) {
        this.sdRed = sdRed;
    }

    public int getSdGreen() {
        return sdGreen;
    }

    public void setSdGreen(int sdGreen) {
        this.sdGreen = sdGreen;
    }

    public int getSdBlue() {
        return sdBlue;
    }

    public void setSdBlue(int sdBlue) {
        this.sdBlue = sdBlue;
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

