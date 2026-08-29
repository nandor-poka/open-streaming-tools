package com.openstreamingtools.backend.services.stagelinq;

import com.openstreamingtools.backend.dj.stagelinq.DenonUnit;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class DirectoryService {

    public static final int SERVICE_REQUEST = 2;
    public static final int SERVICE_ANNOUNCEMENT = 0;
    public static final int TIMESTAMP = 1;


    public static Map<UUID, DenonUnit> connectedUnits = new HashMap();

    public static void  addUnit(DenonUnit unit){
        if (hasUnit(unit.getDeviceID())){
            return;
        }
        connectedUnits.put(unit.getDeviceID(),unit);
        log.debug("Added unit {}",unit);
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

    public static void clearUnits(){
        connectedUnits.clear();
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
