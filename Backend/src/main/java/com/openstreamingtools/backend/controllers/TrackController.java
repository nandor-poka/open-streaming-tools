package com.openstreamingtools.backend.controllers;

import com.openstreamingtools.backend.config.OSTConfiguration;
import com.openstreamingtools.backend.db.entities.Track;
import com.openstreamingtools.backend.db.repositories.TrackRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log
public class TrackController {

    @Autowired
    private TrackRepository trackRepository;

    @GetMapping(value = "/api/getTracksForPlaylist/{playlistID}", produces = "application/json")
    public List<Track> getTracksByPlaylist(@PathVariable int playlistID){
        OSTConfiguration.settings.setPlaylistID(playlistID);
        log.info("Getting tracks for playlist: " + playlistID);
        return trackRepository.findAllByPlaylistID(playlistID);
    }
}
