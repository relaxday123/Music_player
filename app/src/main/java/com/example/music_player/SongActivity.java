package com.example.music_player;

import static com.example.music_player.MainActivity.musicFiles;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SongActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    static MusicAdapter musicAdapter;
//    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
//    public static final String MUSIC_FILE = "STORED_MUSIC";
//    public static final String SONG_NAME = "SONG_NAME";
//    public static final String SONG_ARTIST = "SONG_ARTIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            musicAdapter = new MusicAdapter(getApplicationContext(), musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,
                    false));
        }
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