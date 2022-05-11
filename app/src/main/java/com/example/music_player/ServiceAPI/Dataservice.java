package com.example.music_player.ServiceAPI;

import com.example.music_player.Model.Album;
import com.example.music_player.Model.Playlist;
import com.example.music_player.Model.Song;
import com.example.music_player.Model.Theme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Dataservice {

    @GET("GetSong.php")
    Call<List<Song>> GetSong();
    @GET("theme.php")
    Call<List<Theme>> GetTheme();
    @GET("playlist.php")
    Call<List<Playlist>> GetPlaylist();
    @GET("album.php")
    Call<List<Album>> GetAlbum();
    @GET("RandomSong.php")
    Call<List<Song>> GetRandomSong();

    @FormUrlEncoded
    @POST("ListSong.php")
    Call<List<Song>> GetListSongFromTheme(@Field("IdTheme") String IdTheme);

    @FormUrlEncoded
    @POST("ListSong.php")
    Call<List<Song>> GetListSongFromPlaylist(@Field("IdPlaylist") String IdPlaylist);

    @FormUrlEncoded
    @POST("ListSong.php")
    Call<List<Song>> GetListSongFromAlbum(@Field("IdAlbum") String IdAlbum);

    @FormUrlEncoded
    @POST("Likes.php")
    Call<String> UpdateLikes(@Field("Likes") String Likes, @Field("IdSong") String IdSong );
}
