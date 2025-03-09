package org.Webbtj.DiggarenManar.service;


import org.Webbtj.DiggarenManar.domain.DiggarenAPI;
import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DiggarenAPIController {

    private final RadioService radioService;
    private final SpotifyServi spotifyService;

    public DiggarenAPIController(RadioService radioService, SpotifyServi spotifyService) {
        this.radioService = radioService;
        this.spotifyService = spotifyService;
    }

    @GetMapping("/songinfo/{songType}")
    public DiggarenAPI getSongInfo(@PathVariable String songType, @RequestParam String channel) {
        boolean fetchCurrent = "currentsong".equalsIgnoreCase(songType);
        RadioSong radioSong = radioService.getSongByType(channel, fetchCurrent);
        if (radioSong == null) {
            return new DiggarenAPI("No song found:(", "", "");
        }

        Map<String, String> spotifyData = spotifyService.searchTrackDetails(radioSong.getArtist() + " " + radioSong.getTitle());

        DiggarenAPI diggarenAPI = new DiggarenAPI();
        diggarenAPI.setTitle(spotifyData.getOrDefault("trackName", radioSong.getTitle()));
        diggarenAPI.setArtist(spotifyData.getOrDefault("artist",radioSong.getArtist()));
        diggarenAPI.setPlayedTime(radioSong.getPlayedTime());
        diggarenAPI.setChannelName(channel);
        diggarenAPI.setSpotifyLink(spotifyData.getOrDefault("spotifyUrl", ""));

        return diggarenAPI;
    }
}
