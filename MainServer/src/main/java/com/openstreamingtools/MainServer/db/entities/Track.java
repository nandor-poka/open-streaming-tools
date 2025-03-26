package com.openstreamingtools.MainServer.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
public class Track {

    @Id
    private Integer id;
    private Integer playOrder;
    private Integer length;
    private Integer bpm;
    private Integer year;
    private String path;
    private String filename;
    private Integer bitrate;
    private Float bpmAnalyzed;
    private Integer albumArtId;
    private Integer fileBytes;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String comment;
    private String label;
    private String composer;
    private String remixer;
    private Integer key;
    private Integer rating;
    private String albumArt;
    private Date timeLastPlayed;
    private Boolean isPlayed;
    private String fileType;
    private Boolean isAnalyzed;
    private Date dateCreated;
    private Date dateAdded;
    private Boolean isAvailable;
    private Boolean isMetadataOfPackedTrackChanged;
    private Boolean isPerfomanceDataOfPackedTrackChanged;
    private Integer playedIndicator;
    private Boolean isMetadataImported;
    private Integer pdbImportKey;
    private String streamingSource;
    private String uri;
    private Boolean isBeatGridLocked;
    private String originDatabaseUuid;
    private Integer originTrackId;
    private Integer streamingFlags;
    private Boolean explicitLyrics;
    private Date lastEditTime;

    public Track() {
    }


}
