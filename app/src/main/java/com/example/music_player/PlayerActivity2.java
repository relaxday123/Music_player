package com.example.music_player;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.music_player.Adapter.PlayerVPager;
import com.example.music_player.Fragment.F_Disk;
import com.example.music_player.Fragment.F_PlayerListSong;
import com.example.music_player.Model.Song;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
    ViewPager viewPager;
    public static PlayerVPager playerVPager;
    F_Disk f_disk;
    F_PlayerListSong f_playerListSong;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GetDataFromIntent();
        initViews();
        eventClick();
    }

    private void eventClick() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(playerVPager.getItem(0) != null) {
                    Log.d("BBBBBB","okay");
                    if (listsongs.size() > 0){
                        Log.d("BBBBBB","okay+1");
                        f_disk.PlayNhac(listsongs.get(0).getSongPic(),listsongs.get(0).getSongName(),listsongs.get(0).getArtist());
                        handler.removeCallbacks(this);
                    } else {
                        handler.postDelayed(this, 500);
                        Log.d("BBBBBB","okay-1");
                    }
                }
            }
        },500);
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseBtn.setIconResource(R.drawable.ic_play);
                } else {
                    mediaPlayer.start();
                    playPauseBtn.setIconResource(R.drawable.ic_pause);
                }
            }
        });
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        //listsongs.clear();
        if (intent != null) {
            if (intent.hasExtra("Song")) {
                Song song = intent.getParcelableExtra("Song");
                Toast.makeText(this, song.getSongName(), Toast.LENGTH_SHORT).show();
                listsongs.add(song);
                Log.d("Tagadded","songadded");
                Log.d("BBB",listsongs.get(0).getSongName());
            }

            if (intent.hasExtra("allSongs")) {
                ArrayList<Song> songs = intent.getParcelableArrayListExtra("allSongs");
                listsongs = songs;
                Log.d("Tagadded","songadded");
                Log.d("BBB",listsongs.get(0).getSongName());
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
        viewPager = findViewById(R.id.playervp);
//        frag_bottom_player = findViewById(R.id.frag_bottom_player);
        timerBtn = findViewById(R.id.timerBtnPA);
        shareBtn = findViewById(R.id.shareBtnPA);
//        getSupportActionBar().hide();
        f_disk = new F_Disk();
        f_playerListSong = new F_PlayerListSong();
        playerVPager = new PlayerVPager(getSupportFragmentManager());
        playerVPager.AddFrag(f_disk);
        playerVPager.AddFrag(f_playerListSong);
        viewPager.setAdapter(playerVPager);
        f_disk = (F_Disk) playerVPager.getItem(0);
//        Log.d("ooooo",listsongs.get(0).getSongName());
        if (listsongs.size() > 0 ) {
//            getSupportActionBar().setTitle();
            Log.d("BBBBBB","hassong+1");
            new playMp3().execute(listsongs.get(0).getSongURL());
            playPauseBtn.setIconResource(R.drawable.ic_pause);
        }
    }

    class playMp3 extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String song) {
            super.onPostExecute(song);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.setDataSource(song);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            Log.d("BBBBBB","songplayed");
            SongDuration();
        }
    }

    private void SongDuration() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        duration_total.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }
}