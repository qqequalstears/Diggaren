package org.Webbtj.DiggarenManar.service;



import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*@RestController
@RequestMapping("/api/song")
public class SongRestAPI {

    private final RadioService radioService;
    private final SpotifyService spotifyService;

    @Autowired
    public SongRestAPI(RadioService radioService, SpotifyService spotifyService) {
        this.radioService = radioService;
        this.spotifyService = spotifyService;
    }

    @GetMapping
    public ResponseEntity<?> getSong(@RequestParam String channel) {
        RadioSong radioSong = radioService.getCurrentSong(channel);
        if (radioSong == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingen låt hittades");
        }
        String query = radioSong.getArtist() + " " + radioSong.getTitle();
        String spotifyLink = spotifyService.generateSpotifySearchLink(query);

        Map<String, Object> result = new HashMap<>();
        result.put("artist", radioSong.getArtist());
        result.put("låt", radioSong.getTitle());
        result.put("kanal", channel);
        result.put("klockslag", radioSong.getPlayedTime());
        result.put("spotifyLänk", spotifyLink);

        return ResponseEntity.ok(result);
    }
}

 */
