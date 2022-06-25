package com.example.music_player;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.music_player.databinding.ActivityFavoriteBinding;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;
    private FavoriteAdapter adapter;

    static ArrayList<MusicFiles> favoriteSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtnFA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.favoriteRV.setHasFixedSize(true);
        binding.favoriteRV.setItemViewCacheSize(13);
        binding.favoriteRV.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new FavoriteAdapter(this, favoriteSongs);
        binding.favoriteRV.setAdapter(adapter);
        getSupportActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(SongActivity.MUSIC_LAST_PLAYED, MODE_PRIVATE);
        String path = preferences.getString(SongActivity.MUSIC_FILE, null);
        String artist = preferences.getString(SongActivity.SONG_ARTIST, null);
        String song_name = preferences.getString(SongActivity.SONG_NAME, null);
        if (path != null) {
            SongActivity.SHOW_MINI_PLAYER = true;
            SongActivity.PATH_TO_FRAG = path;
            SongActivity.ARTIST_TO_FRAG = artist;
            SongActivity.SONG_NAME_TO_FRAG = song_name;
        } else {
            SongActivity.SHOW_MINI_PLAYER = false;
            SongActivity.PATH_TO_FRAG = null;
            SongActivity.ARTIST_TO_FRAG = null;
            SongActivity.SONG_NAME_TO_FRAG = null;
        }
    }
}