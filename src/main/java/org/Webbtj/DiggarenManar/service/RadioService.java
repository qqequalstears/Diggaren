package org.Webbtj.DiggarenManar.service;


import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.Webbtj.DiggarenManar.domain.RadioSongResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RadioService {

    private final RestTemplate restTemplate;

    public RadioService() {
        this.restTemplate = new RestTemplate();
    }

    public RadioSong getCurrentSong(String channel) {
        String url = "https://api.sr.se/api/v2/playlist?channel=" + channel + "&format=json";
        try {
            ResponseEntity<RadioSongResponse> response = restTemplate.getForEntity(url, RadioSongResponse.class);
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
