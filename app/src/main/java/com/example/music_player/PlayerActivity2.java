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
import java.util.Random;

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
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    int pos = 0;
    boolean repeatBoolean = false;
    boolean shuffleBoolean = false;
    boolean next = false;


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
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    repeatBtn.setImageResource(R.drawable.ic_repeat);
                } else {
                    repeatBoolean = true;
                    repeatBtn.setImageResource(R.drawable.ic_repeat_on);
                }
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleBoolean) {
                    shuffleBoolean = false;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle);
                } else {
                    shuffleBoolean = true;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
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
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listsongs.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (pos < (listsongs.size())) {
                        //nextBtn.setIconResource(R.drawable.ic_next);
                        pos++;
                        if (repeatBoolean == true){
                            if (pos == 0) {
                                pos = listsongs.size();
                            }
                            pos--;
                        }
                        if (shuffleBoolean == true) {
                            Random random1 = new Random();
                            int index = random1.nextInt(listsongs.size());
                            if ( index == pos){
                                pos = index - 1;
                            }
                            pos = index;
                        }
                        if (pos > (listsongs.size() - 1)) {
                            pos = 0;
                        }
                        new playMp3().execute(listsongs.get(pos).getSongURL());
                        f_disk.PlayNhac(listsongs.get(pos).getSongPic(),listsongs.get(pos).getSongName(),listsongs.get(pos).getArtist());
                        curTime();
                    }
                }
                nextBtn.setClickable(false);
                prevBtn.setClickable(false);
                Handler h1 = new Handler();
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextBtn.setClickable(true);
                        prevBtn.setClickable(true);
                    }
                },3000);
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listsongs.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    if (pos < (listsongs.size())) {
                        //nextBtn.setIconResource(R.drawable.ic_next);
                        pos--;
                        if (pos < 0) {
                            pos = listsongs.size() - 1;
                        }
                        if (repeatBoolean == true){
                            pos++;
                        }
                        if (shuffleBoolean == true) {
                            Random random1 = new Random();
                            int index = random1.nextInt(listsongs.size());
                            if ( index == pos){
                                pos = index - 1;
                            }
                            pos = index;
                        }
                        new playMp3().execute(listsongs.get(pos).getSongURL());
                        f_disk.PlayNhac(listsongs.get(pos).getSongPic(),listsongs.get(pos).getSongName(),listsongs.get(pos).getArtist());
                        curTime();
                    }
                }
                nextBtn.setClickable(false);
                prevBtn.setClickable(false);
                Handler h1 = new Handler();
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextBtn.setClickable(true);
                        prevBtn.setClickable(true);
                    }
                },3000);
            }
        });
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        listsongs.clear();
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
        getSupportActionBar().hide();
        f_disk = new F_Disk();
        f_playerListSong = new F_PlayerListSong();
        playerVPager = new PlayerVPager(getSupportFragmentManager());
        playerVPager.AddFrag(f_disk);
        playerVPager.AddFrag(f_playerListSong);
        viewPager.setAdapter(playerVPager);
//        getSupportActionBar().hide();
        f_disk = (F_Disk) playerVPager.getItem(0);
//        Log.d("ooooo",listsongs.get(0).getSongName());
        if (listsongs.size() > 0 ) {
//            getSupportActionBar().setTitle();
//            mediaPlayer.stop();
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
            curTime();
        }

    }

    private void SongDuration() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        duration_total.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }
    private void curTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    duration_played.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this,300);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        },300);
        Handler h1 = new Handler();
        h1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true)  {
                    if (pos < (listsongs.size())) {
                        nextBtn.setIconResource(R.drawable.ic_next);
                        pos++;
                        if (repeatBoolean == true){
                            if (pos == 0) {
                                pos = listsongs.size();
                            }
                            pos--;
                        }
                        if (shuffleBoolean == true) {
                            Random random1 = new Random();
                            int index = random1.nextInt(listsongs.size());
                            if ( index == pos){
                                pos = index - 1;
                            }
                            pos = index;
                        }
                        if (pos > (listsongs.size() - 1)) {
                            pos = 0;
                        }
                        new playMp3().execute(listsongs.get(pos).getSongURL());
                        f_disk.PlayNhac(listsongs.get(pos).getSongPic(),listsongs.get(pos).getSongName(),listsongs.get(pos).getArtist());
                    }
                nextBtn.setClickable(false);
                prevBtn.setClickable(false);
                Handler h1 = new Handler();
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextBtn.setClickable(true);
                        prevBtn.setClickable(true);
                    }
                },3000);
                next = false;
                h1.removeCallbacks(this);
                } else {
                    h1.postDelayed(this,1000);
                }
            }
        },1000);
    }
}