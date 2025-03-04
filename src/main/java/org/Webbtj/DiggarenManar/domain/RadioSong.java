package org.Webbtj.DiggarenManar.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;


public class RadioSong {
    private String title;
    private String artist;
    private String playedTime;



    private String startTime;
    private String endTime;

    public RadioSong() {}

    public RadioSong(String title, String artist, String playedTime) {
        this.title = title;
        this.artist = artist;
        this.playedTime = playedTime;
    }
    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @XmlElement
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPlayedTime() {
        return playedTime;
    }

    public void setPlayedTime(String playedTime) {
        this.playedTime = playedTime;
    }

    @XmlElement
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    @XmlElement
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
