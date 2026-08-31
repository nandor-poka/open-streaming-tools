package com.openstreamingtools.backend.dj;

/**
 * Enumeration of DJ equipment unit types that can be discovered via StageLinQ protocol.
 * Represents different types of DJ equipment in a typical setup.
 */
public enum UnitType {
    /** Software-based DJ application or control application */
    SOFTWARE,
    /** Audio mixer unit */
    MIXER,
    /** DJ player/deck unit */
    PLAYER,
    /** Controller device */
    CONTROLLER,
    /** Other or unknown unit type */
    OTHER
}
