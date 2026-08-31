package com.openstreamingtools.backend.twitch;


import lombok.Data;

/**
 * Data transfer object for Twitch channel points custom rewards.
 * Represents a reward that viewers can redeem using channel points.
 */
@Data
public class Reward {
    /** Unique reward identifier */
    private String id;
    /** Display title of the reward */
    private String title;
    /** Prompt or description shown to users */
    private String prompt;
    /** Channel points cost to redeem */
    private int cost;
}
