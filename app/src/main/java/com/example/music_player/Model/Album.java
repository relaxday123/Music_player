package com.example.music_player.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Album {

    @SerializedName("IdAlbum")
    @Expose
    private String idAlbum;
    @SerializedName("AlbumName")
    @Expose
    private String albumName;
    @SerializedName("ArtistName")
    @Expose
    private String artistName;
    @SerializedName("AlbumPic")
    @Expose
    private String albumPic;

    public String getIdAlbum() {
    return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
    this.idAlbum = idAlbum;
    }

    public String getAlbumName() {
    return albumName;
    }

    public void setAlbumName(String albumName) {
    this.albumName = albumName;
    }

    public String getArtistName() {
    return artistName;
    }

    public void setArtistName(String artistName) {
    this.artistName = artistName;
    }

    public String getAlbumPic() {
    return albumPic;
    }

    public void setAlbumPic(String albumPic) {
    this.albumPic = albumPic;
    }

}