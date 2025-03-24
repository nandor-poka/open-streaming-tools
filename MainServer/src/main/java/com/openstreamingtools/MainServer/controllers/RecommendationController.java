package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.db.entities.Track;
import com.openstreamingtools.MainServer.db.repositories.TrackRepository;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class RecommendationController {


    @Autowired
    private TrackRepository trackRepository;

    @GetMapping(value = "/api/getInKeyRecommendation/{key}", produces = "application/json")
    public void getInKeyRecommendation(@PathVariable int key){
        List<Track> tracks = trackRepository.findAllByPlaylistID(17);
        List<Track> tracksInKey = new ArrayList<>();
        for (Track track : tracks){
            if(track.getKey() == key){
                tracksInKey.add(track);
            }
        }
        Collections.shuffle(tracksInKey);
        StringBuilder twitchMessage = new StringBuilder("I recommend the following songs to you to request:\n");
        int i=1;
        for (Track track :  tracksInKey.subList(0, 5)) {
            twitchMessage.append(i++).append(". ").append(track.getTitle()).append(" - ").append(track.getArtist()).append(",\n");
        }
        TwitchUtils.sendToChat(twitchMessage.toString());

      // return tracks.subList(0, 5);
    }

    @GetMapping(value = "/api/getBroadInKeyRecommendation/{key}", produces = "application/json")
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
