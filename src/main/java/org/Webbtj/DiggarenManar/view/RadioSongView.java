package org.Webbtj.DiggarenManar.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

    private final Button fetchButton = new Button("Fetch Current Song");
    private final Label songInfo = new Label();
    private final Anchor spotifyLinkAnchor = new Anchor("", "Open in Spotify");

    public RadioSongView(RadioService radioService, SpotifyService spotifyService) {
        this.radioService = radioService;
        this.spotifyService = spotifyService;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        fetchButton.addClickListener(e -> fetchCurrentSong());

        add(new H1("Current Radio Song"), fetchButton, songInfo, spotifyLinkAnchor);
    }

    private void fetchCurrentSong() {
        String channel = "2576";
        RadioSong radioSong = radioService.getCurrentSong(channel);
        if (radioSong != null) {
            String artist = radioSong.getArtist();
            String title = radioSong.getTitle();
            String playedTime = radioSong.getPlayedTime();
            String spotifyLink = spotifyService.generateSpotifySearchLink(artist + " " + title);

            songInfo.setText("Now Playing: " + artist + " - " + title + " (" + playedTime + ")");
            spotifyLinkAnchor.setHref(spotifyLink);
            spotifyLinkAnchor.setVisible(true);
        } else {
            songInfo.setText("No song information available for the specified channel.");
            spotifyLinkAnchor.setVisible(false);
        }
    }
}
