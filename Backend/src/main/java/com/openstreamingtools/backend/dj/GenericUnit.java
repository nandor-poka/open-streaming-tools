package com.openstreamingtools.backend.dj;

import com.openstreamingtools.backend.dj.stagelinq.ModelType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.jackson.JacksonComponent;

/**
 * This class represents a generic unit that is used as an interface towards the frontend
 * A unit can be a Player, a controller a mixer, or a software
 * Subclasses of this class should specialize by setting the correct values to the attributes
 * and provide additional methods and attributes as needed
 */
@JacksonComponent
public class GenericUnit {
    public UnitType type ;
    public ModelType modelType;
    public String longName;
    // TODO maybe store published services here
    @Setter
    @Getter
    public String version;
    public int deckCount;
    public boolean acknowledged = false;

}
