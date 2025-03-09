package org.Webbtj.DiggarenManar.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class RadioSong {
    private String title;
    private String artist;
    private String playedTime;
    private String channelName;


    public RadioSong() {
    }

    public RadioSong(String title, String artist, String playedTime, String channelName) {
        this.title = title;
        this.artist = artist;
        this.playedTime = convertToTime(playedTime);
        if(channelName.equals("164")){
            channelName = "P3";
        }else if(channelName.equals("163")){
            channelName = "P2";}
        else if(channelName.equals("132")){
            channelName = "P1";
        }
        this.channelName = channelName;

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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    private String convertToTime(String rawTimestamp) {
        if (rawTimestamp == null || rawTimestamp.length() < 8 || !rawTimestamp.contains("Date")) {
            return "N/A";
        }

        try {
            String millisString = rawTimestamp.substring(6, rawTimestamp.length() - 2);
            long millis = Long.parseLong(millisString);
            Instant instant = Instant.ofEpochMilli(millis);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return dateTime.format(timeFormatter);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to parse timestamp: " + rawTimestamp);
            return "N/A";
        }
    }

}