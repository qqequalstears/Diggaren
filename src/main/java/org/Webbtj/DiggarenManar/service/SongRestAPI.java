package org.Webbtj.DiggarenManar.service;

import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.Webbtj.DiggarenManar.service.RadioService;
import org.Webbtj.DiggarenManar.service.SpotifyServi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/song")
public class SongRestAPI {
    private final RadioService radioService;
    private final SpotifyServi spotifyService;

    @Autowired
    public SongRestAPI(RadioService radioService, SpotifyServi spotifyService) {
        this.radioService = radioService;
        this.spotifyService = spotifyService;
    }

    @GetMapping
    public ResponseEntity<?> getSong(@RequestParam String channel, @RequestParam boolean fetchCurrent) {
        RadioSong radioSong = radioService.getSongByType(channel, fetchCurrent);
        if (radioSong == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No song found:(");
        }

        Map<String, String> spotifyData = spotifyService.searchTrackDetails(radioSong.getArtist() + " " + radioSong.getTitle());

        Map<String, Object> result = new HashMap<>();
        result.put("Artist", radioSong.getArtist());
        result.put("Song", radioSong.getTitle());
        result.put("channel", channel);
        result.put("playedTime", radioSong.getPlayedTime());

        if (!spotifyData.containsKey("error")) {
            result.put("Track", spotifyData.get("trackName"));
            result.put("Artist", spotifyData.get("artist"));
            result.put("album", spotifyData.get("album"));
            result.put("releaseDate", spotifyData.get("releaseDate"));
            result.put("spotifyUrl", spotifyData.get("spotifyUrl"));
        } else {
            result.put("spotifyError", "nothing found on Spotify");
        }

        return ResponseEntity.ok(result);
    }
}
