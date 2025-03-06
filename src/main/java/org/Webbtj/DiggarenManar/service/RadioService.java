package org.Webbtj.DiggarenManar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    private static final String SR_API_URL = "http://api.sr.se/api/v2/playlists/rightnow?channelid="; // 🔹 **Lagt till konstanter för API URL**

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
                System.err.println(" API returned status: " + response.getStatusCode());
                return getDefaultSong();
            }

            System.out.println("API Response: " + response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode playlistNode = jsonNode.path("playlist");

            // Choose correct song type based on user selection
            JsonNode songNode = fetchCurrent ? playlistNode.path("song") : playlistNode.path("previoussong");

            if (!songNode.isMissingNode()) {
                return new RadioSong(
                        songNode.path("title").asText("Unknown Song"),
                        songNode.path("artist").asText("Unknown Artist"),
                        songNode.path("starttimeutc").asText("N/A")
                );
            } else {
                System.err.println("⚠️ No song found in API response.");
            }

        } catch (RestClientException e) {
            System.err.println(" API request error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return getDefaultSong();
    }
    private RadioSong getDefaultSong(){
        return new RadioSong("no song found", "unkown artist", "N/A");
    }
}