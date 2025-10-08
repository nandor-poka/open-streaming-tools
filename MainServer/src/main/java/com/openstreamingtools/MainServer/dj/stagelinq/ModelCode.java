package com.openstreamingtools.MainServer.dj.stagelinq;

/**
 * Enum class to represent model codes
 *
 */
public enum ModelCode {
    JC11("JC11"),
    JC16("JC16"),
    JC20("JC20"),
    JP07("JP07"),
    JP08("JP08"),
    JP11("JP11"),
    JP13("JP13"),
    JP14("JP14"),
    JP20("JP20"),
    JP21("JP21"),
    NH08("NH08"),
    NH09("NH09"),
    NH10("NH10"),
    JM08("JM08"),
    JM10("JM10"),
    UNKOWN("UNKNOWN");

    private final String modelCode;
    ModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public static ModelCode getByValue(String modelCode){
        for (ModelCode mc : values()){
            if (mc.modelCode.equals(modelCode)){
                return mc;
            }
        }
        return UNKOWN;
    }
}
