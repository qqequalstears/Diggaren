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

            if(response.getStatusCode() != HttpStatus.OK){
                System.err.println("API returnerade status: " + response.getStatusCode());
                return getDefaultSong();
            }

            System.out.println("📡 API Response: " + response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode playlistNode = jsonNode.path("playlist");
            JsonNode songNode = playlistNode.path("song");


            if (songNode.isMissingNode()) { // Om det inte finns en "song", ta "previoussong"
                return new RadioSong(
                songNode.path("Title").asText("uknown song"),
                songNode.path("artist").asText("unkown artist"),
                songNode.path("start time").asText("N/A")
                );

            } else {
                System.err.println(" no song was found");
            }


        } catch (JsonProcessingException e) { // 🔹 **Hantera JSON-fel**
            System.err.println(" JSON-fel vid parsing av Sveriges Radio API: " + e.getMessage());
        } catch (RestClientException e) { // 🔹 **Hantera nätverksfel**
            System.err.println(" Fel vid anrop till Sveriges Radio API: " + e.getMessage());
        } catch (Exception e) { // 🔹 **Hantera generella fel**
            System.err.println(" Oväntat fel i RadioService: " + e.getMessage());
        }

        return getDefaultSong();
    }

    private RadioSong getDefaultSong(){
        return new RadioSong("no song found", "unkown artist", "N/A");
    }
}