package org.Webbtj.DiggarenManar;

import com.vaadin.flow.component.UI;
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

@Route("main")
public class MainView extends VerticalLayout {

    private TextField channelField;
    private Button fetchButton;
    private Label songInfo;
    private Anchor spotifyLinkAnchor;

    public MainView() {

        channelField = new TextField("Ange radiokanal");
        fetchButton = new Button("Hämta låt");
        songInfo = new Label();
        spotifyLinkAnchor = new Anchor("", "Öppna i Spotify");
        spotifyLinkAnchor.setTarget("_blank");

        fetchButton.addClickListener(e -> fetchSong());
        add(channelField, fetchButton, songInfo, spotifyLinkAnchor);


        UI.getCurrent().navigate("radiosong");
    }

    private void fetchSong() {
        String channel = channelField.getValue();
        try {
            String url = "http://localhost:8080/api/song?channel=" + URLEncoder.encode(channel, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> songData = mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
                String artist = (String) songData.get("artist");
                String title = (String) songData.get("låt");
                String time = (String) songData.get("klockslag");
                String spotifyLink = (String) songData.get("spotifyLänk");
                songInfo.setText("Nu spelar: " + artist + " - " + title + " (" + time + ")");
                spotifyLinkAnchor.setHref(spotifyLink);
            } else {
                songInfo.setText("Fel vid hämtning av låt");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            songInfo.setText("Ett fel uppstod");
        }
    }
}
