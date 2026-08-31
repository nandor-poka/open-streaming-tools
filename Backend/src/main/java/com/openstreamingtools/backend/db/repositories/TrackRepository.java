package com.openstreamingtools.backend.db.repositories;

import com.openstreamingtools.backend.db.entities.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;


/**
 * Spring Data JPA repository for Track entity operations.
 * Provides CRUD operations and custom query methods for retrieving Track objects
 * from the database, including playlist-specific and key-based queries.
 */
public interface TrackRepository extends JpaRepository<Track, Integer> {

    /**
     * Retrieves all tracks associated with a specific playlist.
     * Uses a native SQL query to join Track and PlaylistEntity tables.
     *
     * @param playlistId the ID of the playlist to retrieve tracks for
     * @return list of Track objects in the playlist
     */
    @NativeQuery(value = "SELECT t.*, p.listId, p.trackId from Track t, PlaylistEntity p where t.id=p.trackId and p.listId=? ")
    List<Track> findAllByPlaylistID(int playlistId);

    /**
     * Finds all tracks with a specific musical key.
     *
     * @param key the musical key to search for
     * @return list of tracks matching the key
     */
    List<Track> findByKey(Integer key);

    /**
     * Finds all tracks with keys within a specified range (inclusive).
     *
     * @param keyAfter the minimum key value
     * @param keyBefore the maximum key value
     * @return list of tracks with keys between the specified range
     */
    List<Track> findByKeyBetween(Integer keyAfter, Integer keyBefore);




}
