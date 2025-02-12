package com.openstreamingtools.MainServer.dj;

import com.openstreamingtools.MainServer.dj.stagelinq.ModelType;
import com.openstreamingtools.MainServer.dj.stagelinq.UnitType;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class GenericUnit {
    public UnitType type ;
    public ModelType modelType;
    public String LongName;
    public String version;
    public int deckCount;
    public boolean acknowledged = false;
// TODO maybe store published services here
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
