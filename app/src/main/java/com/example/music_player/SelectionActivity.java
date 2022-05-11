package com.example.music_player;

import static com.example.music_player.MainActivity.musicFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.music_player.databinding.ActivityPlaylistDetailsBinding;
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
        setContentView(R.layout.activity_selection);

        recyclerView = findViewById(R.id.selectionRV);
        recyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            adapter = new MusicAdapter(this,true, musicFiles);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                    false));
        }

//        binding.selectionRV.setItemViewCacheSize(10);
//        binding.selectionRV.setHasFixedSize(true);
//        binding.selectionRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
//                false));
//        adapter = new MusicAdapter(this,true, musicFiles);
//        binding.selectionRV.setAdapter(adapter);
        binding.backBtnSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().hide();

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
                adapter.updateList(MainActivity.musicFiles);
                return true;
            }
        });
    }
}