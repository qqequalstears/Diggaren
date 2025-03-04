package org.Webbtj.DiggarenManar.service;


import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.Webbtj.DiggarenManar.domain.RadioSongResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

@Service
public class RadioService {

    private final RestTemplate restTemplate;

    public RadioService() {
        this.restTemplate = new RestTemplate();

    }

    public RadioSong getCurrentSong(String channel) {
        // Bygg upp URL utifrån vald kanal
        //String url = "http://api.sr.se/api/v2/playlist?channel=" + channel + "&format=json";
        String url = "http://api.sr.se/api/v2/playlists/rightnow?channelid=2576";
        try {
            HttpHeaders headers = new HttpHeaders(); // Skapar headers för HTTP-anropet
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // Anger att vi vill få svaret i JSON-format
            HttpEntity<String> entity = new HttpEntity<>(headers); // Skapar en HTTP-request med headers men ingen body

            ResponseEntity<RadioSongResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, RadioSongResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) { // Kontroll av HTTP-statuskod och att response har data
                if (!response.getBody().getPlaylist().isEmpty()) { // Kontroll om spellistan från API-svaret inte är tom
                    return response.getBody().getPlaylist().get(0); // Returnerar den första låten i spellistan
                } else {
                    System.err.println("⚠️ Sveriges Radio API svarade men innehåller ingen låtdata."); // Skriver ut varning om svaret saknar låtdata
                }
            } else {
                System.err.println("⚠️ Sveriges Radio API returnerade en oväntad statuskod: " + response.getStatusCode()); // Skriver ut varning om statuskoden inte är 200-299
            }
        }catch (RestClientException e) { // Om API-anropet misslyckas på grund av nätverksproblem eller om API:et är nere
            System.err.println(" Fel vid anrop till Sveriges Radio API: " + e.getMessage()); // Skriver ut felmeddelande om API-anropet misslyckas
        }
// 🔹 **Fångar alla oväntade fel**
        catch (Exception e) { // Hanterar alla andra typer av fel
            System.err.println(" Oväntat fel i RadioService: " + e.getMessage()); // Skriver ut generellt felmeddelande
        }

// 🔹 **Returnerar null istället för att krascha**
        return null; // Returnerar null om något går fel istället för att applikationen kraschar
        }
    }


