package com.example.music_player;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_player.Model.Song;

import java.util.ArrayList;

public class PlayerActivity2 extends AppCompatActivity {
    ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);
        Intent intent = getIntent();
        if (intent.hasExtra("Song")) {
            Song songs = intent.getParcelableExtra("Song");
            Toast.makeText(this, songs.getSongName(),Toast.LENGTH_SHORT).show();
        }

        if (intent.hasExtra("allSongs")) {
            songs = intent.getParcelableArrayListExtra("allSongs");
            for (int i = 0; i < songs.size(); i++){
                Log.d("ADDD",songs.get(i).getSongName());
            }

        }
    }
}