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

    public RadioSong getCurrentSong(String channel) {
        String url = SR_API_URL + channel + "&format=json"; // 🔹 **Bygger korrekt API-URL**

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 🔹 **Logga API-svaret för debugging**
            System.out.println("📡 API Response: " + response.getBody());

            // 🔹 **Konvertera JSON-svaret till ett objekt**
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode playlistNode = jsonNode.path("playlist");

            // 🔹 **Hämta låten om den finns, annars ta `previoussong`**
            JsonNode songNode = playlistNode.path("song");
            if (songNode.isMissingNode()) { // Om det inte finns en "song", ta "previoussong"
                songNode = playlistNode.path("previoussong");
            }

            if (!songNode.isMissingNode()) {
                RadioSong radioSong = new RadioSong();
                radioSong.setArtist(songNode.path("artist").asText("Okänd artist"));
                radioSong.setTitle(songNode.path("title").asText("Okänd låt"));
                radioSong.setPlayedTime(songNode.path("starttimeutc").asText("N/A"));
                return radioSong;
            } else {
                System.err.println("⚠️ Varken 'song' eller 'previoussong' hittades i API-svaret.");
            }

        } catch (JsonProcessingException e) { // 🔹 **Hantera JSON-fel**
            System.err.println(" JSON-fel vid parsing av Sveriges Radio API: " + e.getMessage());
        } catch (RestClientException e) { // 🔹 **Hantera nätverksfel**
            System.err.println(" Fel vid anrop till Sveriges Radio API: " + e.getMessage());
        } catch (Exception e) { // 🔹 **Hantera generella fel**
            System.err.println(" Oväntat fel i RadioService: " + e.getMessage());
        }

        // 🔹 **Om vi misslyckas, returnera en placeholder-låt istället för null**
        RadioSong fallbackSong = new RadioSong();
        fallbackSong.setArtist("Okänd artist");
        fallbackSong.setTitle("Ingen låt hittades");
        fallbackSong.setPlayedTime("N/A");

        return fallbackSong;
    }
}