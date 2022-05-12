package com.example.music_player;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_player.Model.Song;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity2 extends AppCompatActivity {

    public static ArrayList<Song> listsongs = new ArrayList<>();
    TextView song_name, song_artist, duration_played, duration_total;
    CircleImageView cover_art;
    ImageView repeatBtn, shuffleBtn, timerBtn, shareBtn;
    ImageButton returnBtn;
    ExtendedFloatingActionButton prevBtn;
    static ExtendedFloatingActionButton playPauseBtn;
    ExtendedFloatingActionButton nextBtn;
    SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);
        initViews();
        GetDataFromIntent();

    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        listsongs.clear();
        if (intent != null) {
            if (intent.hasExtra("Song")) {
                Song song = intent.getParcelableExtra("Song");
                Toast.makeText(this, song.getSongName(), Toast.LENGTH_SHORT).show();
                listsongs.add(song);
            }

            if (intent.hasExtra("allSongs")) {
                ArrayList<Song> songs = intent.getParcelableArrayListExtra("allSongs");
                listsongs = songs;
            }
        }
    }

    private void initViews() {
        song_name = findViewById(R.id.song_name);
        song_artist = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.duration_played);
        duration_total = findViewById(R.id.duration_total);
        cover_art = findViewById(R.id.cover_art);
        prevBtn = findViewById(R.id.prevBtn);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        nextBtn = findViewById(R.id.nextBtn);
        repeatBtn = findViewById(R.id.repeatBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        seekBar = findViewById(R.id.seekBar);
        returnBtn = findViewById(R.id.returnBtn);
//        frag_bottom_player = findViewById(R.id.frag_bottom_player);
        timerBtn = findViewById(R.id.timerBtnPA);
        shareBtn = findViewById(R.id.shareBtnPA);
//        getSupportActionBar().hide();
    }
}