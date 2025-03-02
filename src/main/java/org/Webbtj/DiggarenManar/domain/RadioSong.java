package org.Webbtj.DiggarenManar.domain;

public class RadioSong {
    private String title;
    private String artist;
    private String playedTime;

    public RadioSong() {}

    public RadioSong(String title, String artist, String playedTime) {
        this.title = title;
        this.artist = artist;
        this.playedTime = playedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
}
