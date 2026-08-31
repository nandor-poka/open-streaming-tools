package com.openstreamingtools.backend.dj.stagelinq;

/**
 * Enumeration of DJ equipment model codes recognized by StageLinQ protocol.
 * Codes represent different Pioneer CDJ and mixer models.
 */
public enum ModelCode {
    /** Pioneer model code JC11 */
    JC11("JC11"),
    /** Pioneer model code JC16 */
    JC16("JC16"),
    /** Pioneer model code JC20 */
    JC20("JC20"),
    /** Pioneer model code JP07 */
    JP07("JP07"),
    /** Pioneer model code JP08 */
    JP08("JP08"),
    /** Pioneer model code JP11 */
    JP11("JP11"),
    /** Pioneer model code JP13 */
    JP13("JP13"),
    /** Pioneer model code JP14 */
    JP14("JP14"),
    /** Pioneer model code JP20 (Denon SC Live 4) */
    JP20("JP20"),
    /** Pioneer model code JP21 */
    JP21("JP21"),
    /** Pioneer model code NH08 */
    NH08("NH08"),
    /** Pioneer model code NH09 */
    NH09("NH09"),
    /** Pioneer model code NH10 */
    NH10("NH10"),
    /** Pioneer model code JM08 */
    JM08("JM08"),
    /** Pioneer model code JM10 */
    JM10("JM10"),
    /** Unknown or unrecognized model code */
    UNKNOWN("UNKNOWN");

    private final String modelCode;

    /**
     * Creates a model code with its string representation.
     *
     * @param modelCode the model code string
     */
    ModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    /**
     * Retrieves the model code enum by its string value.
     *
     * @param modelCode the string representation to look up
     * @return the corresponding ModelCode, or UNKOWN if not found
     */
    public static ModelCode getByValue(String modelCode){
        for (ModelCode mc : values()){
            if (mc.modelCode.equals(modelCode)){
                return mc;
            }
        }
        return UNKNOWN;
    }
}
