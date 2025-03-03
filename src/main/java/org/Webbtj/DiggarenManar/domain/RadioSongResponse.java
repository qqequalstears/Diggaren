package org.Webbtj.DiggarenManar.domain;



import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;



@XmlRootElement(name = "sr")
public class RadioSongResponse {
    private List<RadioSong> playlist;

    public RadioSongResponse() {}


    @XmlElement(name = "playlist")
    public List<RadioSong> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<RadioSong> playlist) {
        this.playlist = playlist;
    }
}
