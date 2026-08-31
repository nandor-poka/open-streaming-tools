package com.openstreamingtools.backend.messages.stagelinqmessages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeatData {
    private double beat;
    private double totalBeats;
    private double BPM;
    private double samples;
}
