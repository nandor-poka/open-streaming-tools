package com.openstreamingtools.backend.db.repositories;

import com.openstreamingtools.backend.db.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for Playlist entity operations.
 * Provides CRUD (Create, Read, Update, Delete) operations and query methods
 * for Playlist objects. Automatically managed by Spring Data JPA framework.
 */
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

}
