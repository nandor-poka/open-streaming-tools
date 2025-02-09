package com.openstreamingtools.MainServer.services.stagelinq;

import com.openstreamingtools.MainServer.messages.stagelinqmessages.Service;
import com.openstreamingtools.MainServer.messages.stagelinqmessages.ServiceType;
import com.openstreamingtools.MainServer.utils.Utils;

public class StateMapService extends Service {

    public static final String MAGIC_MARKER = "smaa";
    public static final int MAGIC_MARKER_INTERVAL = 0x000007d2;
    public static final int MAGIC_MARKER_JSON = 0x00000000;

    private boolean isDeviceService = false;
    public StateMapService() {
        super();
        this.type= ServiceType.STATEMAP;
    }

    public StateMapService(ServiceType serviceType, int port) {
        super();
        this.type = serviceType;
        this.unitPort = port;
        this.isDeviceService = true;
    }

    public boolean isDeviceService() {
        return isDeviceService;
    }
}
