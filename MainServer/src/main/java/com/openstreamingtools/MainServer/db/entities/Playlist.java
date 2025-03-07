package com.openstreamingtools.MainServer.db.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Date;

@Entity
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

    public Playlist(int id, String title, int parentListId, boolean isPersisted, int nextListId, Date lastEditTime, boolean isExplicitlyExported) {
        this.id = id;
        this.title = title;
        this.parentListId = parentListId;
        this.isPersisted = isPersisted;
        this.nextListId = nextListId;
        this.lastEditTime = lastEditTime;
        this.isExplicitlyExported = isExplicitlyExported;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getParentListId() {
        return parentListId;
    }

    public void setParentListId(int parentListId) {
        this.parentListId = parentListId;
    }

    public boolean isPersisted() {
        return isPersisted;
    }

    public void setPersisted(boolean persisted) {
        isPersisted = persisted;
    }

    public int getNextListId() {
        return nextListId;
    }

    public void setNextListId(int nextListId) {
        this.nextListId = nextListId;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public boolean isExplicitlyExported() {
        return isExplicitlyExported;
    }

    public void setExplicitlyExported(boolean explicitlyExported) {
        isExplicitlyExported = explicitlyExported;
    }
}
