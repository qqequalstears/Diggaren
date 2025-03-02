package org.Webbtj.DiggarenManar.domain;

import java.util.List;

public class RadioSongResponse {
    private List<RadioSong> playlist;

    public RadioSongResponse() {}

    public List<RadioSong> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<RadioSong> playlist) {
        this.playlist = playlist;
    }
}
