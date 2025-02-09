package com.openstreamingtools.MainServer.messages.stagelinqmessages;

import java.util.UUID;

public class StateData {
    UUID deviceId; // the uuid of the unit that sent the message
    String name; // name of the state;
    int interval; // the interval associated with the state
    String jsonString; // the json message associated with the state
}