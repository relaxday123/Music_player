package com.example.music_player.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Song implements Serializable {

    @SerializedName("IdSong")
    @Expose
    private int idSong;
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

    public Song(int IdSong, String SongName, String SongPic, String Artist, String SongURL, String Likes) {
        this.idSong = IdSong;
        this.songName = SongName;
        this.songPic = SongPic;
        this.artist = Artist;
        this.songURL = SongURL;
        this.likes = Likes;
    }


//    protected Song(Parcel in) {
//        idSong = in.readInt();
//        songName = in.readString();
//        songPic = in.readString();
//        artist = in.readString();
//        songURL = in.readString();
//        likes = in.readString();
//    }

//    public static final Creator<Song> CREATOR = new Creator<Song>() {
//        @Override
//        public Song createFromParcel(Parcel in) {
//            return new Song(in);
//        }
//
//        @Override
//        public Song[] newArray(int size) {
//            return new Song[size];
//        }
//    };


    public int getIdSong() {
    return idSong;
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

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(idSong);
//        parcel.writeString(songName);
//        parcel.writeString(songPic);
//        parcel.writeString(artist);
//        parcel.writeString(songURL);
//        parcel.writeString(likes);
//
//    }
}