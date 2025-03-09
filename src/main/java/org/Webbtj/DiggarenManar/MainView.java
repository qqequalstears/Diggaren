package org.Webbtj.DiggarenManar;

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
    private Label currentSongLabel;
    private Label lastPlayedSongLabel;
    private Anchor spotifyLinkAnchor;

    public MainView() {
        // 🔹 Skapa UI-komponenter
        channelField = new TextField("Ange radiokanal");
        fetchButton = new Button("Hämta låt");
        currentSongLabel = new Label();
        lastPlayedSongLabel = new Label();
        spotifyLinkAnchor = new Anchor("", "Öppna i Spotify");
        spotifyLinkAnchor.setTarget("_blank");

        fetchButton.addClickListener(e -> fetchSongs());

        add(channelField, fetchButton, currentSongLabel, lastPlayedSongLabel, spotifyLinkAnchor);
    }

    private void fetchSongs() {
        String channel = channelField.getValue().trim();
        if (channel.isEmpty()) {
            currentSongLabel.setText("⚠️ Ange ett kanal-ID!");
            return;
        }

        try {
            // 🔹 Hämta nuvarande låt
            fetchSong(channel, true);
            // 🔹 Hämta senaste spelade låt
            fetchSong(channel, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            currentSongLabel.setText("❌ Ett fel uppstod vid hämtning av låtar.");
        }
    }

    private void fetchSong(String channel, boolean fetchCurrent) {
        try {
            String url = "http://localhost:8080/api/song?channel=" + URLEncoder.encode(channel, StandardCharsets.UTF_8) + "&current=" + fetchCurrent;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> songData = mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
                String artist = (String) songData.get("artist");
                String title = (String) songData.get("title");
                String time = (String) songData.get("playedTime");
                String spotifyLink = (String) songData.get("spotifyLink");

                if (fetchCurrent) {
                    currentSongLabel.setText("🎵 Nu spelas: " + artist + " - " + title + " (Starttid: " + time + ")");
                } else {
                    lastPlayedSongLabel.setText("⏮ Senast spelad: " + artist + " - " + title + " (Starttid: " + time + ")");
                }

                spotifyLinkAnchor.setHref(spotifyLink);
            } else {
                currentSongLabel.setText("⚠️ Fel vid hämtning av låt.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            currentSongLabel.setText(" Ett fel uppstod.");
        }
    }
}