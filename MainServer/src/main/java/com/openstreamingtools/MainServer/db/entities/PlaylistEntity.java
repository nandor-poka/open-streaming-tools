package com.openstreamingtools.MainServer.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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

    public Integer getMembershipReference() {
        return membershipReference;
    }

    public void setMembershipReference(Integer membershipReference) {
        this.membershipReference = membershipReference;
    }

    public Integer getNextEntityId() {
        return nextEntityId;
    }

    public void setNextEntityId(Integer nextEntityId) {
        this.nextEntityId = nextEntityId;
    }

    public String getDatabaseUuid() {
        return databaseUuid;
    }

    public void setDatabaseUuid(String databaseUuid) {
        this.databaseUuid = databaseUuid;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
