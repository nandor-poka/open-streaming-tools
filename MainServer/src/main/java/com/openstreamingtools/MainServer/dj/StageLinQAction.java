package com.openstreamingtools.MainServer.dj;

import org.springframework.context.annotation.Bean;


public enum StageLinQAction {
    HOWDY("DISCOVERER_HOWDY"),EXI("DISCOVERER_EXIT"), UNKNOWN("UNKNOWN");
    public final String action;

    StageLinQAction(String action){
        this.action = action;
    }

    public static StageLinQAction getByValue(String value){
        for (StageLinQAction sla : values()){
            if (sla.action.equals(value)) {
                return sla;
            }
        }
        return UNKNOWN;
    }
}
