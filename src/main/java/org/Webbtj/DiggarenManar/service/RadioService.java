package org.Webbtj.DiggarenManar.service;


import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.Webbtj.DiggarenManar.domain.RadioSongResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

@Service
public class RadioService {

    private final RestTemplate restTemplate;

    public RadioService() {
        this.restTemplate = new RestTemplate();

    }

    public RadioSong getCurrentSong(String channel) {
        String url = "https://api.sr.se/api/v2/playlist?channel=" + channel + "&format=json";
        // Bygg upp URL utifrån vald kanal
        //String url = "http://api.sr.se/api/v2/playlist?channel=" + channel + "&format=json";
        url = "http://api.sr.se/api/v2/playlists/rightnow?channelid=2576";
        try {
            String xmlResponse = restTemplate.getForObject(url, String.class);
            System.out.println("Raw XML Response: " + xmlResponse);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<RadioSongResponse> entity = new HttpEntity<>(headers);

            ResponseEntity<RadioSongResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, RadioSongResponse.class);
            if (response.getStatusCode().is2xxSuccessful() &&
                    response.getBody() != null &&
                    !response.getBody().getPlaylist().isEmpty()) {
                return response.getBody().getPlaylist().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
