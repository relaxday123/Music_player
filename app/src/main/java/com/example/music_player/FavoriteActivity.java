package com.example.music_player;

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
    }
}