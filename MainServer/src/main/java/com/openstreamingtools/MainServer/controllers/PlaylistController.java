package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.db.entities.Playlist;
import com.openstreamingtools.MainServer.db.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @GetMapping(value= "/api/getPlaylists", produces = "application/json")
    public List<Playlist> getPlaylists(){
        return playlistRepository.findAll();
    }
}
