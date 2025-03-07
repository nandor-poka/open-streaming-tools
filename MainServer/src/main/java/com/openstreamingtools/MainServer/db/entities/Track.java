package com.openstreamingtools.MainServer.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Date;

@Entity
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(Integer playOrder) {
        this.playOrder = playOrder;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Float getBpmAnalyzed() {
        return bpmAnalyzed;
    }

    public void setBpmAnalyzed(Float bpmAnalyzed) {
        this.bpmAnalyzed = bpmAnalyzed;
    }

    public Integer getAlbumArtId() {
        return albumArtId;
    }

    public void setAlbumArtId(Integer albumArtId) {
        this.albumArtId = albumArtId;
    }

    public Integer getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(Integer fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getRemixer() {
        return remixer;
    }

    public void setRemixer(String remixer) {
        this.remixer = remixer;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public Date getTimeLastPlayed() {
        return timeLastPlayed;
    }

    public void setTimeLastPlayed(Date timeLastPlayed) {
        this.timeLastPlayed = timeLastPlayed;
    }

    public Boolean getPlayed() {
        return isPlayed;
    }

    public void setPlayed(Boolean played) {
        isPlayed = played;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Boolean getAnalyzed() {
        return isAnalyzed;
    }

    public void setAnalyzed(Boolean analyzed) {
        isAnalyzed = analyzed;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Boolean getMetadataOfPackedTrackChanged() {
        return isMetadataOfPackedTrackChanged;
    }

    public void setMetadataOfPackedTrackChanged(Boolean metadataOfPackedTrackChanged) {
        isMetadataOfPackedTrackChanged = metadataOfPackedTrackChanged;
    }

    public Boolean getPerfomanceDataOfPackedTrackChanged() {
        return isPerfomanceDataOfPackedTrackChanged;
    }

    public void setPerfomanceDataOfPackedTrackChanged(Boolean perfomanceDataOfPackedTrackChanged) {
        isPerfomanceDataOfPackedTrackChanged = perfomanceDataOfPackedTrackChanged;
    }

    public Integer getPlayedIndicator() {
        return playedIndicator;
    }

    public void setPlayedIndicator(Integer playedIndicator) {
        this.playedIndicator = playedIndicator;
    }

    public Boolean getMetadataImported() {
        return isMetadataImported;
    }

    public void setMetadataImported(Boolean metadataImported) {
        isMetadataImported = metadataImported;
    }

    public Integer getPdbImportKey() {
        return pdbImportKey;
    }

    public void setPdbImportKey(Integer pdbImportKey) {
        this.pdbImportKey = pdbImportKey;
    }

    public String getStreamingSource() {
        return streamingSource;
    }

    public void setStreamingSource(String streamingSource) {
        this.streamingSource = streamingSource;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getBeatGridLocked() {
        return isBeatGridLocked;
    }

    public void setBeatGridLocked(Boolean beatGridLocked) {
        isBeatGridLocked = beatGridLocked;
    }

    public String getOriginDatabaseUuid() {
        return originDatabaseUuid;
    }

    public void setOriginDatabaseUuid(String originDatabaseUuid) {
        this.originDatabaseUuid = originDatabaseUuid;
    }

    public Integer getOriginTrackId() {
        return originTrackId;
    }

    public void setOriginTrackId(Integer originTrackId) {
        this.originTrackId = originTrackId;
    }

    public Integer getStreamingFlags() {
        return streamingFlags;
    }

    public void setStreamingFlags(Integer streamingFlags) {
        this.streamingFlags = streamingFlags;
    }

    public Boolean getExplicitLyrics() {
        return explicitLyrics;
    }

    public void setExplicitLyrics(Boolean explicitLyrics) {
        this.explicitLyrics = explicitLyrics;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
}
