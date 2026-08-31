package com.openstreamingtools.backend.dj;

import com.openstreamingtools.backend.dj.stagelinq.DenonUnit;
import com.openstreamingtools.backend.dj.stagelinq.ModelCode;
import com.openstreamingtools.backend.dj.stagelinq.ModelType;

import java.util.AbstractMap;
import java.util.Map;


/**
 * Utility methods and constants for handling DJ equipment units.
 * Provides mappings of unit model codes to their implementations
 * and constants for StageLinQ protocol communication.
 */
public class UnitUtils {

    /** StageLinQ message frame start identifier */
    public final static String STAGELINQ_MESSAGE_START = "airD";
    /** Log message for StageLinQ device discovery */
    public final static String STAGELINQ_UNIT_DISCOVERED = "StageLinQ device discovered";

    /**
     * Map of DJ unit implementations indexed by their model codes.
     * Enables quick lookup of unit instances to handle device-specific behavior.
     */
    public static final Map<ModelCode, GenericUnit> unitMapping = Map.ofEntries(
            new AbstractMap.SimpleEntry<ModelCode, GenericUnit>(ModelCode.JP20,
                    new DenonUnit(UnitType.CONTROLLER,ModelType.SCX4,"DenonDJ SC Live 4", 4))
    );

}
