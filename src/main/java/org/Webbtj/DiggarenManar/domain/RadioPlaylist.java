package org.Webbtj.DiggarenManar.domain;

import jakarta.xml.bind.annotation.XmlElement;

public class RadioPlaylist {
    private RadioSong previoussong;
    private RadioSong song;
    // Possibly a Channel object if you need <channel> data

    @XmlElement
    public RadioSong getPrevioussong() {
        return previoussong;
    }
    public void setPrevioussong(RadioSong previoussong) {
        this.previoussong = previoussong;
    }

    @XmlElement
    public RadioSong getSong() {
        return song;
    }
    public void setSong(RadioSong song) {
        this.song = song;
    }
}
