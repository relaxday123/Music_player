package com.example.music_player.ServiceAPI;

import com.example.music_player.Model.Playlist;
import com.example.music_player.Model.Song;
import com.example.music_player.Model.Theme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Dataservice {

    @GET("GetSong.php")
    Call<List<Song>> GetSong();
    @GET("theme.php")
    Call<List<Theme>> GetTheme();
    @GET("playlist.php")
    Call<List<Playlist>> GetPlaylist();
}
