package org.Webbtj.DiggarenManar.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RadioService {

    private final RestTemplate restTemplate;
    private static final String SR_API_URL = "http://api.sr.se/api/v2/playlists/rightnow?channelid=";

    public RadioService() {
        this.restTemplate = new RestTemplate();
    }

    public RadioSong getSongByType(String channel, boolean fetchCurrent) {
        String url = SR_API_URL + channel + "&format=json";
        System.out.println("API Request: " + url);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                System.err.println("API returned status: " + response.getStatusCode());
                return getDefaultSong();
            }

            String responseBody = response.getBody();
            System.out.println("API Response: " + responseBody);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode playlistNode = jsonNode.path("playlist");

            JsonNode currentSongNode = playlistNode.path("song");
            JsonNode latestPlayedNode = playlistNode.path("previoussong");

            // Log available data for debugging
            System.out.println("Current Song Exists: " + !currentSongNode.isMissingNode());
            System.out.println("Latest Played Exists: " + !latestPlayedNode.isMissingNode());

            // Ensure we fetch the right type of song
            JsonNode songNode;
            if (fetchCurrent) {
                songNode = currentSongNode; // Always use "song" for current song
            } else {
                // Use "previoussong" if available, otherwise return "song"
                songNode = !latestPlayedNode.isMissingNode() ? latestPlayedNode : currentSongNode;
                System.out.println("No `previoussong` found, using `song` instead.");
            }

            // Ensure the selected song is valid
            if (!songNode.isMissingNode() && songNode.has("title") && songNode.has("artist")) {
                return new RadioSong(
                        songNode.path("title").asText("Unknown Song"),
                        songNode.path("artist").asText("Unknown Artist"),
                        songNode.path("starttimeutc").asText("N/A")
                );
            } else {
                System.err.println("No valid song found in API for: " + (fetchCurrent ? "Current Song" : "Latest Played Song"));
            }

        } catch (RestClientException e) {
            System.err.println("API request error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return getDefaultSong();
    }


    private RadioSong getDefaultSong() {
        return new RadioSong("Song not found", "Unknown Artist", "N/A");
    }
}
