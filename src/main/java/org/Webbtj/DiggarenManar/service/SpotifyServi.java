package org.Webbtj.DiggarenManar.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Webbtj.DiggarenManar.service.spotifyAuthService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyServi {
    private static final String SEARCH_URL = "https://api.spotify.com/v1/search";
    private final spotifyAuthService authService = new spotifyAuthService();

    public Map<String, String> searchTrackDetails(String query) {
        String accessToken = authService.getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(SEARCH_URL)
                .queryParam("q", query)
                .queryParam("type", "track")
                .queryParam("limit", "1");

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, String.class);

        Map<String, String> trackDetails = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.readTree(response.getBody());
            JsonNode track = jsonNode.path("tracks").path("items").get(0);
            if (track != null) {
                trackDetails.put("trackName", track.path("name").asText("Unknown"));
                trackDetails.put("artist", track.path("artists").get(0).path("name").asText("Unknown"));
                trackDetails.put("album", track.path("album").path("name").asText("Unknown"));
                trackDetails.put("releaseDate", track.path("album").path("release_date").asText("Unknown"));
                trackDetails.put("spotifyUrl", track.path("external_urls").path("spotify").asText("Unknown"));
            }
        } catch (Exception e) {
            trackDetails.put("error", "No track found");
        }

        return trackDetails;
    }
}
