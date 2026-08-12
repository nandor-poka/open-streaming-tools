package com.openstreamingtools.MainServer.dj;

import com.openstreamingtools.MainServer.dj.stagelinq.*;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.StageLinQDiscoveryMessage;
import java.util.AbstractMap;
import java.util.Map;


/**
 * Utility method for handling units
 */
public class UnitUtils {

    public final static String STAGELINQ_MESSAGE_START = "airD";
    public final static String STAGELINQ_UNIT_DISCOVERED = "StageLinQ device discovered";

    /**
     * A map that holds unit instances, fot easy lookup using model code as key.
     */
    public static final Map<ModelCode, GenericUnit> unitMapping = Map.ofEntries(
            new AbstractMap.SimpleEntry<ModelCode, GenericUnit>(ModelCode.JP21,
                    new DenonUnit(UnitType.CONTROLLER,ModelType.SCX4,"DenonDJ SC Live 4", 4))
    );

}
