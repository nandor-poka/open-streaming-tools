package com.openstreamingtools.backend.messages.frontend;

import com.openstreamingtools.backend.dj.GenericUnit;
import com.openstreamingtools.backend.messages.MessageToFrontend;
import com.openstreamingtools.backend.messages.MessageType;

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
