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
    
    private RadioSong getDefaultSong(){
        return new RadioSong("no song found", "unkown artist", "N/A");
    }
}