package com.openstreamingtools.MainServer.db.repositories;

import com.openstreamingtools.MainServer.db.entities.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;


public interface TrackRepository extends JpaRepository<Track, Integer> {

   /* @Query(value = "SELECT new TrackDTO(t.id, t.title, t.artist," +
            "t.key, p.id, p.trackId, p.listId) "
            + " from Track t, PlaylistEntity p"
            + " where (t.id=p.trackId "
            + " and p.listId=?1)")*/
    @NativeQuery(value = "SELECT t.*, p.listId, p.trackId from Track t, PlaylistEntity p where t.id=p.trackId and p.listId=? ")
    List<Track> findAllByPlaylistID(int playlistId);


   //

}
