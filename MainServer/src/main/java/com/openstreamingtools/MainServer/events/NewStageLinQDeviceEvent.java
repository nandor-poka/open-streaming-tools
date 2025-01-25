package com.openstreamingtools.MainServer.events;

import com.openstreamingtools.MainServer.dj.stagelinq.DenonUnit;
import org.springframework.context.ApplicationEvent;

public class NewStageLinQDeviceEvent  extends ApplicationEvent {
    private DenonUnit unit;

    public NewStageLinQDeviceEvent(Object source) {
        super(source);
    }

    public NewStageLinQDeviceEvent(Object source, DenonUnit unit) {
        super(source);
        this.unit = unit;
    }

    public DenonUnit getUnit() {
        return unit;
    }
}
