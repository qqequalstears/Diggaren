package org.Webbtj.DiggarenManar.service;


import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class SpotifyService {

    public String generateSpotifySearchLink(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return "https://open.spotify.com/search/" + encodedQuery;
    }
}
