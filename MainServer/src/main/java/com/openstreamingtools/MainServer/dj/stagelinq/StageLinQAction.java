package com.openstreamingtools.MainServer.dj.stagelinq;

/**
 * Enum for storing action found in StageLinQ messages
 */
public enum StageLinQAction {
    HOWDY("DISCOVERER_HOWDY_"),EXIT("DISCOVERER_EXIT_"), UNKNOWN("UNKNOWN");
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

    public String getAction() {
        return action;
    }
}
