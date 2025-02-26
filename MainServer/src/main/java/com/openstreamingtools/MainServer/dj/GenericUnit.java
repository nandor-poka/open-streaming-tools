package com.openstreamingtools.MainServer.dj;

import com.openstreamingtools.MainServer.dj.stagelinq.ModelType;
import com.openstreamingtools.MainServer.dj.stagelinq.UnitType;
import org.springframework.boot.jackson.JsonComponent;

/**
 * This class represents a generic unit that is used as an interface towards the frontend
 * A unit can be a Player, a controller a mixer, or a software
 * Subclasses of this class should specialize by setting the correct values to the attributes
 * and provide additional methods and attributes as needed
 */
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
