package com.openstreamingtools.backend.dj.stagelinq;

/**
 * Enumeration of DJ equipment model types from StageLinQ discovery packets.
 * Maps canonical model names used in the StageLinQ protocol.
 */
public enum ModelType {
    /** Pioneer CDJ/DJ equipment model SCX4 */
    SCX4("scx4"),
    /** Pioneer CDJ/DJ equipment model SCX2 */
    SCX2("scx2"),
    /** Unknown or unrecognized model type */
    UNKOWN("UNKNOWN");

    private String modelType;

    /**
     * Creates a model type with its string representation.
     *
     * @param modelType the model type string
     */
    ModelType(String modelType) {
        this.modelType = modelType;
    }

    /**
     * Retrieves the model type enum by its string value.
     *
     * @param modelType the string representation to look up
     * @return the corresponding ModelType, or UNKOWN if not found
     */
    public static ModelType getByValue(String modelType){
        for (ModelType mt : values()){
            if (mt.modelType.equals(modelType)){
                return mt;
            }
        }
        return UNKOWN;
    }
}
