package com.example.music_player;

import java.lang.ref.Reference;
import java.sql.Ref;
import java.util.ArrayList;

public class MusicFiles {
//    public class Playlist {
//        private String name;
//        private ArrayList<MusicFiles> playlist;
//        private String createdBy;
//        private String createdOn;
//
//        public Playlist() {
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public ArrayList<MusicFiles> getPlaylist() {
//            return playlist;
//        }
//
//        public void setPlaylist(ArrayList<MusicFiles> playlist) {
//            this.playlist = playlist;
//        }
//
//        public String getCreatedBy() {
//            return createdBy;
//        }
//
//        public void setCreatedBy(String createdBy) {
//            this.createdBy = createdBy;
//        }
//
//        public String getCreatedOn() {
//            return createdOn;
//        }
//
//        public void setCreatedOn(String createdOn) {
//            this.createdOn = createdOn;
//        }
//    }
//
//     public class MusicPlaylist {
//        ArrayList<Playlist> ref = new ArrayList<>();
//    }

    private String path;
    private String title;
    private String artist;
    private String album;
    private String duration;
    private String id;

    public MusicFiles(String path, String title, String artist, String album, String duration, String id) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
