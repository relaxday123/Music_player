package com.example.music_player;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.Adapter.SongListAdapter;
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
    SongListAdapter songListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        DataIntent();
        GetData();
        init();
        if (banner != null && !banner.getIdSong().equals("")) {
            setVal(banner.getThemePic(),banner.getThemeName());
            GetDataBanner(banner.getIdTheme());
        }
    }

    private void setVal(String pic,String title) {
        collapsingToolbarLayout.setTitle(title);
        Picasso.with(this).load(pic).into(imgslTheme);
    }

    private void GetDataBanner(String IdTheme) {
        Dataservice dataservice = APIService.getService();
        Call<List<Song>> callback = dataservice.GetListSong(IdTheme);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songs = (ArrayList<Song>) response.body();

                songListAdapter = new SongListAdapter(SongListActivity.this,songs);
                songlist.setLayoutManager(new LinearLayoutManager(SongListActivity.this));
                songlist.setAdapter(songListAdapter);

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
    }



    private void GetData() {
        coordinatorLayout = findViewById(R.id.coodinator);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        songlist = findViewById(R.id.songlist);
        floatingActionButton = findViewById(R.id.fplaybtn);
        imgslTheme = findViewById(R.id.imgslTheme);
        toolbar = findViewById(R.id.toolbar);
    }

    private void DataIntent() {
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("Banner")){
                banner = (Theme) intent.getSerializableExtra("Banner");
            }
        }
    }
}