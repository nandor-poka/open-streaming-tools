package com.openstreamingtools.MainServer.dj;

import com.openstreamingtools.MainServer.messages.stagelinqmessages.StageLinqDiscoveryMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class UnitUtils {

    public final static String STAGELINQ_MESSAGE_START = "airD";
    public final static String STAGELINQ_UNIT_DISCOVERED = "StageLinQ device discovered";

    public static final Map<ModelType, GenericUnit> unitMapping = Map.ofEntries(
            new AbstractMap.SimpleEntry<ModelType, GenericUnit>(ModelType.SCX4,
                    new DenonUnit(UnitType.CONTROLLER,ModelType.SCX4,"DenonDJ SC Live 4"))
    );
    private static final Logger log = LogManager.getLogger(UnitUtils.class);

    public static StageLinqDiscoveryMessage parseStageLinQDiscoveryMessage(byte[] rawMessage){
        //raw message as string airDV$C2{scx4"DISCOVERER_HOWDY_JP21
        //4.2.0=
        StageLinQAction action = StageLinQAction.getByValue(new String(Arrays.copyOfRange(rawMessage,37,69), StandardCharsets.UTF_16LE) );
        ModelType type = ModelType.getByValue(new String(Arrays.copyOfRange(rawMessage,25,33), StandardCharsets.UTF_16LE) );
        // must replace last byte with 0 because in the original message the last byte is padding only and is not UTF-16 encoded
        // for 5 UTF-16 chars we need an array of 10 bytes and the last needs to be fixed.
        byte[] versionRaw = Arrays.copyOfRange(rawMessage,87,97);
        versionRaw[9]=0;
        String version = new String(versionRaw, StandardCharsets.UTF_16LE);
        return new StageLinqDiscoveryMessage(action, type, version);
    }
}
