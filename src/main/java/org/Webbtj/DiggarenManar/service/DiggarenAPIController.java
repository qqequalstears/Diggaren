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
    private final spotifyAuthService spotifyAuthService;

    public DiggarenAPIController(RadioService radioService, SpotifyServi spotifyService, spotifyAuthService spotifyAuthService) {
        this.radioService = radioService;
        this.spotifyService = spotifyService;
        this.spotifyAuthService = spotifyAuthService;
    }

    @GetMapping("/songinfo/{songType}/{channel}")
    public DiggarenAPI getSongInfo(@PathVariable String songType, @PathVariable String channel) {
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
    @PostMapping("/spotify/token")
    public String spotifyToken() {
        return spotifyAuthService.getAccessToken();
    }

    @GetMapping("/spotify/search/{query}")
    public Map<String, String> searchTrackDetails(@PathVariable String query) {

        return spotifyService.searchTrackDetails(query);
    }

    @GetMapping ("/sr/currentsong/{channel}")
    public RadioSong getSRCurrentSong(@PathVariable String channel){
        return radioService.getSongByType(channel, true);
    }

    @GetMapping("/sr/previoussong/{channel}")
    public RadioSong getSRPreviousSong(@PathVariable String channel){
        return radioService.getSongByType(channel, false);
    }

}
