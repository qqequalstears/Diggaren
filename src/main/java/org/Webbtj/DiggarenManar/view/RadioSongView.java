package org.Webbtj.DiggarenManar.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.Webbtj.DiggarenManar.domain.RadioSong;
import org.Webbtj.DiggarenManar.service.RadioService;
import org.Webbtj.DiggarenManar.service.SpotifyService;

@Route("radiosong")
@PageTitle("Current Radio Song | Diggaren Manar")
public class RadioSongView extends VerticalLayout {

    private final RadioService radioService;
    private final SpotifyService spotifyService;

    private final ComboBox<String> channelSelector = new ComboBox<>("Select Channel");
    private final RadioButtonGroup<String> songTypeSelector = new RadioButtonGroup<>();
    private final Button fetchButton = new Button("Fetch Song");
    private final Label songInfo = new Label();
    private final Anchor spotifyLinkAnchor = new Anchor("", "Open in Spotify");

    public RadioSongView(RadioService radioService, SpotifyService spotifyService) {
        this.radioService = radioService;
        this.spotifyService = spotifyService;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        channelSelector.setItems("P1 (132)", "P2 (163)", "P3 (134)");
        channelSelector.setPlaceholder("Choose a Channel");

        songTypeSelector.setLabel("Choose Song Type");
        songTypeSelector.setItems("Current Song", "Latest Played Song");
        songTypeSelector.setValue("Current Song"); // Default selection

        fetchButton.addClickListener(e -> fetchSong());

        add(new H1("Now Playing"), channelSelector, songTypeSelector, fetchButton, songInfo, spotifyLinkAnchor);
    }

    private void fetchSong() {
        String selectedChannel = channelSelector.getValue();
        if (selectedChannel == null) {
            songInfo.setText("select a channel first!");
            return;
        }

        String channelId = selectedChannel.split(" ")[1].replace("(", "").replace(")", "");
        boolean fetchCurrent = songTypeSelector.getValue().equals("Current Song");

        System.out.println("🎵 Fetching: " + (fetchCurrent ? "Current Song" : "Latest Played Song"));

        RadioSong radioSong = radioService.getSongByType(channelId, fetchCurrent);

        String artist = radioSong.getArtist();
        String title = radioSong.getTitle();
        String playedTime = radioSong.getPlayedTime();
        String spotifyLink = spotifyService.generateSpotifySearchLink(artist + " " + title);

        songInfo.setText("🎶" + artist + " - " + title + " (" + playedTime + ")");
        spotifyLinkAnchor.setHref(spotifyLink);
        spotifyLinkAnchor.setVisible(true);
    }
}
