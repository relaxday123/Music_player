package com.example.music_player;

import java.util.ArrayList;

public class Playlist {
    private String name;
    private ArrayList<MusicFiles> playlist;
    private String createdBy;
    private String createdOn;

    public Playlist() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MusicFiles> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<MusicFiles> playlist) {
        this.playlist = playlist;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}

class MusicPlaylist {
    static ArrayList<Playlist> ref = new ArrayList<>();
}
