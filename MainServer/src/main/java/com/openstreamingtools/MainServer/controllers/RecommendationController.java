package com.openstreamingtools.MainServer.controllers;

import com.openstreamingtools.MainServer.config.OSTConfiguration;
import com.openstreamingtools.MainServer.db.entities.Track;
import com.openstreamingtools.MainServer.db.repositories.TrackRepository;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
public class RecommendationController {

    private final String twitchMessageHeader = "I recommend the following songs to you to request:\n";

    @Autowired
    private TrackRepository trackRepository;

    @GetMapping(value = "/api/getInKeyRecommendation/{key}", produces = "application/json")
    public void getInKeyRecommendation(@PathVariable int key){
        StringBuilder twitchMessage = new StringBuilder(twitchMessageHeader);
        int i=1;
        List<Track> trackList = getInKeyTracks(key);
        int limit = Math.min(trackList.size(), 5);
        for (Track track : getInKeyTracks(key).subList(0, limit)) {
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
        log.debug("getting in key songs for playlist id {}", OSTConfiguration.settings.getPlaylistID());
        for (Track track : trackRepository.findAllByPlaylistID(OSTConfiguration.settings.getPlaylistID())){
            if(track.getKey() == key){
                tracksInKey.add(track);
            }
        }
        Collections.shuffle(tracksInKey);
        return tracksInKey;

    }

}
