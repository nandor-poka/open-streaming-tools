package com.openstreamingtools.MainServer.services.stagelinq;

import com.openstreamingtools.MainServer.dj.stagelinq.DenonUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DirectoryService {

    public static final int SERVICE_REQUEST = 2;
    public static final int SERVICE_ANNOUNCEMENT = 0;
    public static final int TIMESTAMP = 1;

    private static final Logger logger = LoggerFactory.getLogger(DirectoryService.class);

    public static Map<UUID, DenonUnit> connectedUnits = new HashMap();

    public static void  addUnit(DenonUnit unit){
        if (hasUnit(unit.getDeviceID())){
            return;
        }
        connectedUnits.put(unit.getDeviceID(),unit);
        logger.debug("Added unit {}",unit);
    }

    public static DenonUnit getUnit(UUID deviceID){
        return connectedUnits.get(deviceID);
    }

    public static boolean hasUnit(UUID deviceID){
        return connectedUnits.containsKey(deviceID);
    }
    public static void removeUnit(UUID deviceID){
        connectedUnits.remove(deviceID);
    }

    public static DenonUnit getUnitByIP(String ip){
        for (DenonUnit unit : connectedUnits.values()){
            if (unit.getIpString().equals(ip)){
                return unit;
            }
        }
        return null;
    }

}
