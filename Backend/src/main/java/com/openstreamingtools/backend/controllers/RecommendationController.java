package com.openstreamingtools.backend.controllers;

import com.openstreamingtools.backend.config.OSTConfiguration;
import com.openstreamingtools.backend.db.entities.Track;
import com.openstreamingtools.backend.db.repositories.TrackRepository;
import com.openstreamingtools.backend.twitch.TwitchUtils;
import com.openstreamingtools.backend.twitch.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * REST controller for music recommendation based on musical key.
 * Recommends tracks from the playlist that match a specified musical key,
 * with optional broadened recommendations including compatible keys.
 * Sends recommendations to Twitch chat via the bot account.
 */
@RestController
@Slf4j
public class RecommendationController {

    private final String twitchMessageHeader = "I recommend the following songs to you to request:\n";

    /** Repository for accessing track data */
    @Autowired
    private TrackRepository trackRepository;

    /**
     * Recommends up to 5 tracks in the specified musical key.
     * Sends recommendations to Twitch chat via bot account.
     *
     * @param key the musical key (typically 0-11 for the 12 semitones)
     */
    @GetMapping(value = "/api/getInKeyRecommendation/{key}", produces = "application/json")
    public void getInKeyRecommendation(@PathVariable int key){
        StringBuilder twitchMessage = new StringBuilder(twitchMessageHeader);
        int i=1;
        List<Track> trackList = getInKeyTracks(key);
        int limit = Math.min(trackList.size(), 5);
        for (Track track : getInKeyTracks(key).subList(0, limit)) {
            twitchMessage.append(i++).append(". ").append(track.getTitle()).append(" - ").append(track.getArtist()).append(",\n");
        }
        TwitchUtils.sendToChat(twitchMessage.toString(), UserType.BOT);
    }

    /**
     * Recommends tracks from a broader key range: 3 in the exact key,
     * 2 in the key above, and 2 in the key below. Accommodates musical harmony concepts.
     * Sends recommendations to Twitch chat via bot account.
     *
     * @param key the primary musical key
     */
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
        TwitchUtils.sendToChat(twitchMessage.toString(), UserType.BOT);

    }

    /**
     * Retrieves all tracks in the configured playlist that match the specified key.
     * Results are shuffled to provide variety in recommendations.
     *
     * @param key the musical key to search for
     * @return list of tracks matching the key, shuffled randomly
     */
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
