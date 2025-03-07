package com.openstreamingtools.MainServer.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class PlaylistEntity {
    @Id
    private Integer id ;
    private Integer listId  ;
    private Integer trackId  ;
    private String databaseUuid   ;
    private Integer nextEntityId    ;
    private Integer membershipReference ;

    public PlaylistEntity() {
    }

    public PlaylistEntity(int id, int listId, int trackId, String databaseUuid, int nextEntityId, int membershipReference) {
        this.id = id;
        this.listId = listId;
        this.trackId = trackId;
        this.databaseUuid = databaseUuid;
        this.nextEntityId = nextEntityId;
        this.membershipReference = membershipReference;
    }

}
