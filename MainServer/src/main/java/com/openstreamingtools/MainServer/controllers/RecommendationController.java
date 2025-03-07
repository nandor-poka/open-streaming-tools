package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.db.entities.Track;
import com.openstreamingtools.MainServer.db.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class RecommendationController {


    @Autowired
    private TrackRepository trackRepository;

    @GetMapping(value = "/getInKeyRecommendation/{key}", produces = "application/json")
    public List<Track> getInKeyRecommendation(@PathVariable int key){
        List<Track> tracks = trackRepository.findByKey(key);
        Collections.shuffle(tracks);
       return tracks.subList(0, 5);
    }

    @GetMapping(value = "/getBroadInKeyRecommendation/{key}", produces = "application/json")
    public List<Track> getInBroadKeyRecommendation(@PathVariable int key){
        List<Track> inKeyTracks = trackRepository.findByKey(key);
        List<Track> inKeyBeforeTracks = trackRepository.findByKey(key-1);
        List<Track> inKeyAfterTracks = trackRepository.findByKey(key+1);
        Collections.shuffle(inKeyTracks);
        Collections.shuffle(inKeyBeforeTracks);
        Collections.shuffle(inKeyAfterTracks);
        List<Track> tracks = inKeyTracks.subList(0, 3);
        tracks.addAll(inKeyBeforeTracks.subList(0, 2));
        tracks.addAll(inKeyAfterTracks.subList(0, 2));
        return tracks;
    }
}
