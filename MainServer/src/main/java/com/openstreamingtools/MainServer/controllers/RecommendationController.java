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

    private final String twitchMessageHeader = "I recommend the following songs to you to request:\n";

    @Autowired
    private TrackRepository trackRepository;

    @GetMapping(value = "/api/getInKeyRecommendation/{key}", produces = "application/json")
    public void getInKeyRecommendation(@PathVariable int key){
        StringBuilder twitchMessage = new StringBuilder(twitchMessageHeader);
        int i=1;
        for (Track track : getInKeyTracks(key).subList(0, 5)) {
            twitchMessage.append(i++).append(". ").append(track.getTitle()).append(" - ").append(track.getArtist()).append(",\n");
        }
        TwitchUtils.sendToChat(twitchMessage.toString());
    }

    @GetMapping(value = "/api/getBroadInKeyRecommendation/{key}", produces = "application/json")
    public void getInBroadKeyRecommendation(@PathVariable int key){
        StringBuilder twitchMessage = new StringBuilder(twitchMessageHeader);
        int i=1;
        for (Track track : getInKeyTracks(key).subList(0, 3)) {
            twitchMessage.append(i++).append(". ").append(track.getTitle()).append(" - ").append(track.getArtist()).append(",\n");
        }
        for (Track track : getInKeyTracks(key+1).subList(0, 2)) {
            twitchMessage.append(i++).append(". ").append(track.getTitle()).append(" - ").append(track.getArtist()).append(",\n");
        }
        for (Track track : getInKeyTracks(key-1).subList(0, 2)) {
            twitchMessage.append(i++).append(". ").append(track.getTitle()).append(" - ").append(track.getArtist()).append(",\n");
        }
        TwitchUtils.sendToChat(twitchMessage.toString());

    }

    private List<Track> getInKeyTracks(int key){
        List<Track> tracksInKey = new ArrayList<>();
        for (Track track : trackRepository.findAllByPlaylistID(17)){
            if(track.getKey() == key){
                tracksInKey.add(track);
            }
        }
        Collections.shuffle(tracksInKey);
        return tracksInKey;

    }

}
