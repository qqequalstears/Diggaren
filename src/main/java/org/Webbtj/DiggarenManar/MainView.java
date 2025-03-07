package org.Webbtj.DiggarenManar;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Route("")
public class MainView extends VerticalLayout {

    private TextField channelField;
    private Button fetchButton;
    private Label songInfo;
    private Anchor spotifyLinkAnchor;

    public MainView() {
        // 🔹 Skapa textfält för kanal-ID
        channelField = new TextField("Ange radiokanal");

        // 🔹 Skapa knapp för att hämta låtinfo
        fetchButton = new Button("Hämta låt");

        // 🔹 Skapa etikett för att visa låtinfo
        songInfo = new Label();

        // 🔹 Skapa en länk till Spotify
        spotifyLinkAnchor = new Anchor("", "Öppna i Spotify");
        spotifyLinkAnchor.setTarget("_blank"); // 🔹 Öppnar länken i en ny flik

        // 🔹 Lägg till klicklyssnare för att hämta låten
        fetchButton.addClickListener(e -> fetchSong());

        // 🔹 Lägg till alla komponenter i UI
        add(channelField, fetchButton, songInfo, spotifyLinkAnchor);
        updateSongInfo("164");
    }
    private void updateSongInfo(String channelId) {
        try {
            String url = "http://localhost:8080/api/song?channel=" + channelId;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(URI.create(url))
                                             .GET()
                                             .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                songInfo.setText("⚠️ API svarade med statuskod: " + response.statusCode());
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.has("error")) {
                songInfo.setText("⚠️ " + jsonNode.get("error").asText());
            } else {
                String artist = jsonNode.path("artist").asText("Okänd artist");
                String title = jsonNode.path("title").asText("Okänd låt");
                String playedTime = jsonNode.path("playedTime").asText("N/A");

                // 🔹 Uppdatera UI med låtinfo och klockslag
                songInfo.setText("🎵 " + artist + " - " + title + " (Spelades: " + playedTime + ")");
            }

        } catch (Exception e) {
            songInfo.setText("❌ Fel vid hämtning av låtdata");
            e.printStackTrace();
        }
    }

    private void fetchSong() {
        String channelId = channelField.getValue().trim(); // 🔹 Hämta och trimma kanal-id

        if (channelId.isEmpty()) {
            songInfo.setText("⚠️ Ange ett giltigt kanal-ID!");
            return;
        }

        try {
            String url = "http://localhost:8080/api/song?channel=" + URLEncoder.encode(channelId, StandardCharsets.UTF_8);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(URI.create(url))
                                             .GET()
                                             .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                songInfo.setText("⚠️ API svarade med statuskod: " + response.statusCode());
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.has("error")) {
                songInfo.setText("⚠️ " + jsonNode.get("error").asText());
                spotifyLinkAnchor.setHref(""); // 🔹 Rensa Spotify-länken
            } else {
                String artist = jsonNode.path("artist").asText("Okänd artist");
                String title = jsonNode.path("title").asText("Okänd låt");
                String playedTime = jsonNode.path("playedTime").asText("N/A");
                String spotifyLink = jsonNode.path("spotifyLink").asText("");

                // 🔹 Uppdatera UI med låtinfo och Spotify-länk
                songInfo.setText("🎵 " + artist + " - " + title + " (Spelades: " + playedTime + ")");
                spotifyLinkAnchor.setHref(spotifyLink);
            }

        } catch (Exception e) {
            songInfo.setText(" Fel vid hämtning av låtdata");
            e.printStackTrace();
        }
    }

    private void startAutoUpdate() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    String selectedChannel = channelField.getValue().split(" ")[1].replace("(", "").replace(")", "");
                    updateSongInfo(selectedChannel); // 🔹 Anropar metoden varje gång den körs
                    Thread.sleep(10000); // 🔹 Vänta 10 sekunder innan nästa uppdatering
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
