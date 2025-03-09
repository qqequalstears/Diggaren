package org.Webbtj.DiggarenManar.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

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

            // Kontrollera vilken låt vi ska hämta
            JsonNode songNode = fetchCurrent ? currentSongNode : latestPlayedNode;
            if (songNode.isMissingNode()) {
                songNode = !latestPlayedNode.isMissingNode() ? latestPlayedNode : currentSongNode;
            }

            // Kontrollera om låten har nödvändig data
            if (!songNode.isMissingNode() && songNode.has("title") && songNode.has("artist")) {
                return new RadioSong(
                        songNode.path("title").asText("Okänd låt"),
                        songNode.path("artist").asText("Okänd artist"),
                        convertUnixTime(songNode.path("starttimeutc").asText("N/A")) // 🔹 Konvertera klockslag
                );
            } else {
                System.err.println(" Ingen giltig låt hittades för: " + (fetchCurrent ? "Nuvarande låt" : "Senaste spelade låt"));
            }

        } catch (RestClientException e) {
            System.err.println(" API-fel: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" Oväntat fel: " + e.getMessage());
        }

        return getDefaultSong();
    }

    // 🔹 Konvertera Unix-tid till läsbart format (HH:mm:ss)
    private String convertUnixTime(String unixTime) {
        try {
            String timestampStr = unixTime.replaceAll("[^0-9]", "");
            long timestamp = Long.parseLong(timestampStr);

            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));

            return sdf.format(date);
        } catch (Exception e) {
            return "Okänt klockslag";
        }
    }

    private RadioSong getDefaultSong() {
        return new RadioSong("Låt ej hittad", "Okänd artist", "N/A");
    }
}