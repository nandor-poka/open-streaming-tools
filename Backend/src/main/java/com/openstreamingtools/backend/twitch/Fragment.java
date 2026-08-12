package com.openstreamingtools.backend.twitch;

import lombok.Data;

@Data
public class Fragment {
    private String type;
    private String text;
    private String cheermote;
    private String emote;
    private String mention;
}
