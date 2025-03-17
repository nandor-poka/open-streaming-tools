package com.openstreamingtools.MainServer.db.entities;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
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

    public Track(Integer id, Integer playOrder, Integer length, Integer bpm, Integer year, String path, String filename, Integer bitrate, float bpmAnalyzed, Integer albumArtId, Integer fileBytes, String title, String artist, String album, String genre, String comment, String label, String composer, String remixer, Integer key, Integer rating, String albumArt, Date timeLastPlayed, Boolean isPlayed, String fileType, Boolean isAnalyzed, Date dateCreated, Date dateAdded, Boolean isAvailable, Boolean isMetadataOfPackedTrackChanged, Boolean isPerfomanceDataOfPackedTrackChanged, Integer playedIndicator, Boolean isMetadataImported, Integer pdbImportKey, String streamingSource, String uri, Boolean isBeatGridLocked, String originDatabaseUuid, Integer origIntegerrackId, Integer streamingFlags, Boolean explicitLyrics, Date lastEditTime) {
        this.id = id;
        this.playOrder = playOrder;
        this.length = length;
        this.bpm = bpm;
        this.year = year;
        this.path = path;
        this.filename = filename;
        this.bitrate = bitrate;
        this.bpmAnalyzed = bpmAnalyzed;
        this.albumArtId = albumArtId;
        this.fileBytes = fileBytes;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.comment = comment;
        this.label = label;
        this.composer = composer;
        this.remixer = remixer;
        this.key = key;
        this.rating = rating;
        this.albumArt = albumArt;
        this.timeLastPlayed = timeLastPlayed;
        this.isPlayed = isPlayed;
        this.fileType = fileType;
        this.isAnalyzed = isAnalyzed;
        this.dateCreated = dateCreated;
        this.dateAdded = dateAdded;
        this.isAvailable = isAvailable;
        this.isMetadataOfPackedTrackChanged = isMetadataOfPackedTrackChanged;
        this.isPerfomanceDataOfPackedTrackChanged = isPerfomanceDataOfPackedTrackChanged;
        this.playedIndicator = playedIndicator;
        this.isMetadataImported = isMetadataImported;
        this.pdbImportKey = pdbImportKey;
        this.streamingSource = streamingSource;
        this.uri = uri;
        this.isBeatGridLocked = isBeatGridLocked;
        this.originDatabaseUuid = originDatabaseUuid;
        this.originTrackId = origIntegerrackId;
        this.streamingFlags = streamingFlags;
        this.explicitLyrics = explicitLyrics;
        this.lastEditTime = lastEditTime;
    }

}
