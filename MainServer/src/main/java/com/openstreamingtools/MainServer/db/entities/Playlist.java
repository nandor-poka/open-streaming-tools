package com.openstreamingtools.MainServer.db.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
public class Playlist {

    @Id
    private int id;
    private String title ;
    @Column(name = "parentListId")
    private int parentListId;
    @Column(name = "isPersisted")
    private boolean isPersisted;
    @Column(name = "nextListId")
    private int nextListId;
    @Column(name = "lastEditTime")
    private Date lastEditTime ;
    @Column(name = "isExplicitlyExported")
    private boolean isExplicitlyExported;

    public Playlist() {
    }



}
