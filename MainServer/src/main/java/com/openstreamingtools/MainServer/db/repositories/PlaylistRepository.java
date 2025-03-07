package com.openstreamingtools.MainServer.db.repositories;

import com.openstreamingtools.MainServer.db.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

}
