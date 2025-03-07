package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.db.entities.Track;
import com.openstreamingtools.MainServer.db.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrackController {


    @Autowired
    private TrackRepository trackRepository;

    @GetMapping(value = "/getTracksForPlaylist/{playlistID}", produces = "application/json")
    public List<Track> getTracksByPlaylist(@PathVariable int playlistID){
        return  trackRepository.findAllByPlaylistID(playlistID);
    }
}
