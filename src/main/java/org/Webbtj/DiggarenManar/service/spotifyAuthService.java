package org.Webbtj.DiggarenManar.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class spotifyAuthService {
    private static final String CLIENT_ID = "b4a42a288a4647cca57ca5149615acb0";
    private static final String CLIENT_SECRET = "d6b7a222f8e54a298665e77220bc5a1f";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private String accessToken;
    private long tokenExpirationTime = 0;

    public String getAccessToken() {
        if (System.currentTimeMillis() < tokenExpirationTime) {
            return accessToken;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String auth = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            accessToken = jsonNode.get("access_token").asText();
            int expiresIn = jsonNode.get("expires_in").asInt();
            tokenExpirationTime = System.currentTimeMillis() + (expiresIn * 1000);
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token from Spotify: " + e.getMessage(), e);
        }
    }
}
