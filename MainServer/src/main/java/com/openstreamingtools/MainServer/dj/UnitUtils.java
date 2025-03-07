package com.openstreamingtools.MainServer.dj;

import com.openstreamingtools.MainServer.dj.stagelinq.DenonUnit;
import com.openstreamingtools.MainServer.dj.stagelinq.ModelCode;
import com.openstreamingtools.MainServer.dj.stagelinq.ModelType;
import com.openstreamingtools.MainServer.dj.stagelinq.UnitType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.AbstractMap;
import java.util.Map;


public class UnitUtils {

    public final static String STAGELINQ_MESSAGE_START = "airD";
    public final static String STAGELINQ_UNIT_DISCOVERED = "StageLinQ device discovered";

    public static final Map<ModelCode, GenericUnit> unitMapping = Map.ofEntries(
            new AbstractMap.SimpleEntry<ModelCode, GenericUnit>(ModelCode.JP21,
                    new DenonUnit(UnitType.CONTROLLER,ModelType.SCX4,"DenonDJ SC Live 4", 4))
    );

    private static final Logger log = LogManager.getLogger(UnitUtils.class);


}
