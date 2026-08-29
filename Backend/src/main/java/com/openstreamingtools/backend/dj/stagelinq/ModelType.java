package com.openstreamingtools.backend.dj.stagelinq;

/**
 * Enum class for the canonical model types that are in the discovery packets
 */
public enum ModelType {
    SCX4("scx4"), SCX2("scx2"), UNKOWN("UNKNOWN");

    private String modelType;
    ModelType(String modelType) {
        this.modelType = modelType;
    }

    public static ModelType getByValue(String modelType){
        for (ModelType mt : values()){
            if (mt.modelType.equals(modelType)){
                return mt;
            }
        }
        return UNKOWN;
    }
}
