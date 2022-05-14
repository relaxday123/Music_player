package com.example.music_player;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.databinding.ActivitySelectionBinding;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {
    ActivitySelectionBinding binding;
    static MusicAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.selectionRV.setItemViewCacheSize(30);
        binding.selectionRV.setHasFixedSize(true);
        binding.selectionRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MusicAdapter(this,true, MainActivity.musicFiles);
        binding.selectionRV.setAdapter(adapter);

        binding.backBtnSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        getSupportActionBar().hide();

        binding.searchViewSA.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                ArrayList<MusicFiles> myFiles = new ArrayList<>();
                for (MusicFiles song : MainActivity.musicFiles) {
                    if (song.getTitle().toLowerCase().contains(userInput)) {
                        myFiles.add(song);
                    }
                }
                adapter.updateList(myFiles);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(MainActivity.MUSIC_LAST_PLAYED, MODE_PRIVATE);
        String path = preferences.getString(MainActivity.MUSIC_FILE, null);
        String artist = preferences.getString(MainActivity.SONG_ARTIST, null);
        String song_name = preferences.getString(MainActivity.SONG_NAME, null);
        if (path != null) {
            MainActivity.SHOW_MINI_PLAYER = true;
            MainActivity.PATH_TO_FRAG = path;
            MainActivity.ARTIST_TO_FRAG = artist;
            MainActivity.SONG_NAME_TO_FRAG = song_name;
        } else {
            MainActivity.SHOW_MINI_PLAYER = false;
            MainActivity.PATH_TO_FRAG = null;
            MainActivity.ARTIST_TO_FRAG = null;
            MainActivity.SONG_NAME_TO_FRAG = null;
        }
    }
}