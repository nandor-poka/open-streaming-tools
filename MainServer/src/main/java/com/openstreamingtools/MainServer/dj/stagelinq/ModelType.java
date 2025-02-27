package com.openstreamingtools.MainServer.dj.stagelinq;


public enum ModelType {
    SCX4("scx4"), UNKOWN("UNKNOWN");

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
