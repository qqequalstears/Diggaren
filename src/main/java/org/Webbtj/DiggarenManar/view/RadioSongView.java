package org.Webbtj.DiggarenManar.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.Webbtj.DiggarenManar.service.RadioService;
import org.Webbtj.DiggarenManar.service.SpotifyServi;

import java.util.Map;

@Route("")
@PageTitle("Current Radio Song | Diggaren Manar")
public class RadioSongView extends VerticalLayout {

    private final RadioService radioService;
    private final SpotifyServi spotifyService;

    private final ComboBox<String> channelSelector = new ComboBox<>("Select Channel");
    private final RadioButtonGroup<String> songTypeSelector = new RadioButtonGroup<>();
    private final Button fetchButton = new Button("Fetch Song");
    private final Label songInfo = new Label();
    private final Label albumInfo = new Label();
    private final Label releaseDateInfo = new Label();
    private final Anchor spotifyLinkAnchor = new Anchor("", "Open in Spotify");
    private Div logo = new Div();

    public RadioSongView(RadioService radioService, SpotifyServi spotifyService) {
        this.radioService = radioService;
        this.spotifyService = spotifyService;

        getStyle().set("background-color", "#ff7f27");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        Image logoType = new Image("images/DiggarHead.png", "Diggaren Manar logo");
        logo.add(logoType);

        channelSelector.setItems("P2 (163)", "P3 (164)", "P1 (132)");
        channelSelector.setPlaceholder("Choose a channel");

        songTypeSelector.setLabel("Choose song type");
        songTypeSelector.setItems("Current song", "Latest played song");
        songTypeSelector.setValue("Current song");

        fetchButton.addClickListener(e -> fetchSong());
        add(logo);
        add(new H1(""), channelSelector, songTypeSelector, fetchButton, songInfo, albumInfo, releaseDateInfo, spotifyLinkAnchor);
    }

    private void fetchSong() {
        String selectedChannel = channelSelector.getValue();
        if (selectedChannel == null) {
            songInfo.setText("Select a channel!!");
            return;
        }

        String channelId = selectedChannel.split(" ")[1].replace("(", "").replace(")", "");
        boolean fetchCurrent = songTypeSelector.getValue().equals("Current song");

        RadioSong radioSong = radioService.getSongByType(channelId, fetchCurrent);
        if (radioSong == null) {
            songInfo.setText("song not found on this channel");
            spotifyLinkAnchor.setVisible(false);
            return;
        }

        Map<String, String> spotifyData = spotifyService.searchTrackDetails(radioSong.getArtist() + " " + radioSong.getTitle());

        songInfo.setText("🎵 " + spotifyData.getOrDefault("artist", "Unknown Artist") + " - " +
                spotifyData.getOrDefault("trackName", "Unknown Song"));

        albumInfo.setText("Album: " + spotifyData.getOrDefault("album", "Unknown Album"));
        releaseDateInfo.setText("Release date: " + spotifyData.getOrDefault("releaseDate", "Unknown"));

        String spotifyUrl = spotifyData.getOrDefault("spotifyUrl", "");
        if (!spotifyUrl.isEmpty()) {
            spotifyLinkAnchor.setHref(spotifyUrl);
            spotifyLinkAnchor.setVisible(true);
        } else {
            spotifyLinkAnchor.setVisible(false);
        }
    }
}
