package com.openstreamingtools.MainServer.messages.frontend;

import com.openstreamingtools.MainServer.dj.GenericUnit;
import com.openstreamingtools.MainServer.messages.MessageToFrontend;
import com.openstreamingtools.MainServer.messages.MessageType;

public class UnitData extends MessageToFrontend{
    private GenericUnit unit;

    public UnitData(MessageType type, String message, GenericUnit unit) {
        super(type, message);
        this.unit = unit;
    }

    public GenericUnit getUnit() {
        return unit;
    }
}
