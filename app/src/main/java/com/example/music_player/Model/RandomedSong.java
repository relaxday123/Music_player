package com.example.music_player.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RandomedSong {

    @SerializedName("IdSong")
    @Expose
    private String idSong;
    @SerializedName("SongName")
    @Expose
    private String songName;
    @SerializedName("SongPic")
    @Expose
    private String songPic;
    @SerializedName("Artist")
    @Expose
    private String artist;
    @SerializedName("SongURL")
    @Expose
    private String songURL;
    @SerializedName("Likes")
    @Expose
    private String likes;

    public String getIdSong() {
    return idSong;
    }

    public void setIdSong(String idSong) {
    this.idSong = idSong;
    }

    public String getSongName() {
    return songName;
    }

    public void setSongName(String songName) {
    this.songName = songName;
    }

    public String getSongPic() {
    return songPic;
    }

    public void setSongPic(String songPic) {
    this.songPic = songPic;
    }

    public String getArtist() {
    return artist;
    }

    public void setArtist(String artist) {
    this.artist = artist;
    }

    public String getSongURL() {
    return songURL;
    }

    public void setSongURL(String songURL) {
    this.songURL = songURL;
    }

    public String getLikes() {
    return likes;
    }

    public void setLikes(String likes) {
    this.likes = likes;
    }

}
