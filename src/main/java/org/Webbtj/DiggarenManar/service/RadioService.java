package org.Webbtj.DiggarenManar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import java.util.Collections;

@Service
public class RadioService {

    private final RestTemplate restTemplate;
    private static final String SR_API_URL = "http://api.sr.se/api/v2/playlists/rightnow?channelid="; // 🔹 **Lagt till konstanter för API URL**

    public RadioService() {
        this.restTemplate = new RestTemplate();
    }

    public RadioSong getCurrentSong(String channel) {
        String url = SR_API_URL + channel + "&format=json";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode playlistNode = jsonNode.path("playlist");
            JsonNode songNode = playlistNode.path("song");
            if (songNode.isMissingNode()) {
                songNode = playlistNode.path("previoussong");
            }

            if (!songNode.isMissingNode()) {
                RadioSong radioSong = new RadioSong();
                radioSong.setArtist(songNode.path("artist").asText("Okänd artist"));
                radioSong.setTitle(songNode.path("title").asText("Okänd låt"));
                radioSong.setPlayedTime(convertUnixTime(songNode.path("starttimeutc").asText("N/A"))); // 🔹 NYA KODEN!
                return radioSong;
            } else {
                System.err.println("⚠️ Ingen låt hittades.");
            }

        } catch (Exception e) {
            System.err.println(" Fel vid hämtning av låtdata: " + e.getMessage());
        }

        return null;
    }
    public String convertUnixTime(String unixTime) {
        try {
            // 🔹 Extrahera siffrorna från "/Date(1741172586000)/"
            String timestampStr = unixTime.replaceAll("[^0-9]", "");
            long timestamp = Long.parseLong(timestampStr);

            // 🔹 Skapa en Date från Unix-tiden
            Date date = new Date(timestamp);

            // 🔹 Formatera tiden i HH:mm:ss
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm")); // 🔹 Använd svensk tidzon

            return sdf.format(date);
        } catch (Exception e) {
            return "Okänt klockslag";
        }
    }
}