package com.example.music_player;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.Adapter.SongListAdapter;
import com.example.music_player.Model.Album;
import com.example.music_player.Model.Playlist;
import com.example.music_player.Model.Song;
import com.example.music_player.Model.Theme;
import com.example.music_player.ServiceAPI.APIService;
import com.example.music_player.ServiceAPI.Dataservice;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongListActivity extends AppCompatActivity {


    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView songlist;
    Toolbar toolbar;
    ImageView imgslTheme;
    ArrayList<Song> songs;
    FloatingActionButton floatingActionButton;
    Theme banner;
    Playlist playlist;
    Album album;
    SongListAdapter songListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DataIntent();
        GetData();
        init();
        if (banner != null && !banner.getThemeName().equals("")) {
            setVal(banner.getThemePic(),banner.getThemeName());
            GetDataBanner(banner.getIdTheme());
        }
        if (playlist != null && !playlist.getPlaylistName().equals("")) {
            setVal(playlist.getPlaylistPic(),playlist.getPlaylistName());
            GetDataPlaylist(playlist.getIdPlaylist());
        }
        if (album != null && !album.getAlbumName().equals("")) {
            setVal(album.getAlbumPic(),album.getAlbumName());
            GetDataAlbum(album.getIdAlbum());
        }
    }

    private void GetDataAlbum(String IdAlbum) {
        Dataservice dataservice = APIService.getService();
        Call<List<Song>> callback = dataservice.GetListSongFromAlbum(IdAlbum);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songs = (ArrayList<Song>) response.body();
//                Log.d("BBB",songs.get(0).getSongName());
                songListAdapter = new SongListAdapter(SongListActivity.this,songs);
                songlist.setLayoutManager(new LinearLayoutManager(SongListActivity.this));
                songlist.setAdapter(songListAdapter);
                playAll();
            }
            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
            }
        });
    }

    private void GetDataPlaylist(String IdPlaylist) {
        Dataservice dataservice = APIService.getService();
        Call<List<Song>> callback = dataservice.GetListSongFromPlaylist(IdPlaylist);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songs = (ArrayList<Song>) response.body();
                songListAdapter = new SongListAdapter(SongListActivity.this,songs);
                songlist.setLayoutManager(new LinearLayoutManager(SongListActivity.this));
                songlist.setAdapter(songListAdapter);
                playAll();
            }
            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
            }
        });
    }

    private void setVal(String pic,String title) {
        collapsingToolbarLayout.setTitle(title);
        Picasso.with(this).load(pic).into(imgslTheme);
    }

    private void GetDataBanner(String IdTheme) {
        Dataservice dataservice = APIService.getService();
        Call<List<Song>> callback = dataservice.GetListSongFromTheme(IdTheme);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songs = (ArrayList<Song>) response.body();
                Log.d("ooooo",songs.get(0).getSongName());
                songListAdapter = new SongListAdapter(SongListActivity.this,songs);
                songlist.setLayoutManager(new LinearLayoutManager(SongListActivity.this));
                songlist.setAdapter(songListAdapter);
                playAll();
            }
            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
            }
        });
    }

    private void init() {
//        getSupportActionBar().hide();
        ActionBar actionBar = this.getSupportActionBar();
         if (actionBar != null) {
             setSupportActionBar(toolbar);
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     finish();
                 }
             });
            actionBar.setDisplayHomeAsUpEnabled(true);
         }
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        floatingActionButton.setEnabled(false);
    }



    private void GetData() {
        coordinatorLayout = findViewById(R.id.coodinator);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        songlist = findViewById(R.id.songlist);
        floatingActionButton = findViewById(R.id.fplaybtn);
        imgslTheme = findViewById(R.id.imgslTheme);
    }

    private void DataIntent() {
        Intent intent = getIntent();
//        if (!songs.isEmpty()) songs.clear();
        if (intent != null){
            if (intent.hasExtra("Banner")){
                banner = (Theme) intent.getSerializableExtra("Banner");
            }
            if (intent.hasExtra("itemPlaylist")){
                playlist = (Playlist) intent.getSerializableExtra("itemPlaylist");
            }
            if (intent.hasExtra("itemAlbum")){
                album = (Album) intent.getSerializableExtra("itemAlbum");
            }
        }
    }
    private void playAll() {
        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongListActivity.this,PlayerActivity2.class);
                intent.putExtra("allSongs",songs);
                startActivity(intent);
            }
        });
    }
}