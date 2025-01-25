package com.openstreamingtools.MainServer.dj.stagelinq;

public enum ModelCode {
    JP21("JP21"), UNKOWN("UNKNOWN");

    private String modelCode;
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
