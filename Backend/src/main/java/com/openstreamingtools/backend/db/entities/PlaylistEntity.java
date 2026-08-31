package com.openstreamingtools.backend.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * JPA entity representing the relationship between playlists and tracks.
 * Maps playlist membership associations in the database.
 */
@Setter
@Getter
@Entity
public class PlaylistEntity {
    /** Primary key identifier */
    @Id
    private Integer id ;
    /** Playlist ID that this entry belongs to */
    private Integer listId  ;
    /** Track ID for this playlist membership */
    private Integer trackId  ;
    /** Database UUID for tracking */
    private String databaseUuid   ;
    /** Reference to next entity in linked structure */
    private Integer nextEntityId    ;
    /** Membership reference identifier */
    private Integer membershipReference ;

    /**
     * Default constructor for JPA.
     */
    public PlaylistEntity() {
    }


}
