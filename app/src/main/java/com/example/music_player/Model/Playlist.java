package com.example.music_player.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Playlist implements Serializable {

    @SerializedName("IdPlaylist")
    @Expose
    private String idPlaylist;
    @SerializedName("PlaylistName")
    @Expose
    private String playlistName;
    @SerializedName("PlaylistPic")
    @Expose
    private String playlistPic;
    @SerializedName("PlaylistIcon")
    @Expose
    private String playlistIcon;

    public String getIdPlaylist() {
    return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
    this.idPlaylist = idPlaylist;
    }

    public String getPlaylistName() {
    return playlistName;
    }

    public void setPlaylistName(String playlistName) {
    this.playlistName = playlistName;
    }

    public String getPlaylistPic() {
    return playlistPic;
    }

    public void setPlaylistPic(String playlistPic) {
    this.playlistPic = playlistPic;
    }

    public String getPlaylistIcon() {
    return playlistIcon;
    }

    public void setPlaylistIcon(String playlistIcon) {
    this.playlistIcon = playlistIcon;
    }

}