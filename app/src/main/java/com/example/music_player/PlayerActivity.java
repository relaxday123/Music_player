package com.example.music_player;

import static com.example.music_player.FavoriteActivity.favoriteSongs;
import static com.example.music_player.MainActivity.repeatBoolean;
import static com.example.music_player.MainActivity.shuffleBoolean;
import static com.example.music_player.MusicAdapter.mFiles;
import static com.example.music_player.PlaylistActivity.musicPlaylist;
import static com.example.music_player.PlaylistDetails.currentPlaylistPos;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {

    TextView song_name, song_artist, duration_played, duration_total;
    CircleImageView cover_art;
    ImageView repeatBtn, shuffleBtn, timerBtn, shareBtn;
    ImageButton returnBtn, favBtn;
    ExtendedFloatingActionButton prevBtn;
    static ExtendedFloatingActionButton playPauseBtn;
    ExtendedFloatingActionButton nextBtn;
    SeekBar seekBar;
    static int position = -1;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
//    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread, stopThread;
    public static MusicService musicService;
    FrameLayout frag_bottom_player;
    RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    boolean min15 = false;
    boolean min30 = false;
    boolean min60 = false;
    static boolean isFavorite = false;
    int fIndex = -1;

    public RotateAnimation spinning(RotateAnimation anim) {
        anim.setDuration(10000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);

        return anim;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean fromUser) {
                if (musicService != null && fromUser) {
                    musicService.seekTo(progess * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
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
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("audio/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(listSongs.get(position).getPath()));
                startActivity(Intent.createChooser(shareIntent, "Sharing music file!!!"));
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite) {
                    isFavorite = false;
                    favBtn.setImageResource(R.drawable.ic_favorite_border);
                    favoriteSongs.remove(fIndex);
//                    PlayerActivity.musicService.stopForeground(true);
//                    PlayerActivity.musicService.mediaPlayer.release();
//                    PlayerActivity.musicService = null;
//                    finish();
                } else {
                    isFavorite = true;
                    favBtn.setImageResource(R.drawable.ic_favorite_full);
                    favoriteSongs.add(listSongs.get(position));
                    Log.d("FavSong", listSongs.get(position).toString());
                }
            }
        });
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);;
        dialog.setContentView(R.layout.bottom_sheet_dialog);
        dialog.show();
        dialog.findViewById(R.id.min_15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayerActivity.this, "Music will stop after 15 minutes", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                timerBtn.setImageResource(R.drawable.ic_timer_on);
                min15 = true;

                    Thread newThread = new Thread(() -> {
                        try {
                            Thread.sleep((long) 5000);
                            if (min15) {
                                if (PlayerActivity.musicService != null) {
                                    PlayerActivity.musicService.pause();
                                }
                                finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                newThread.start();
            }
        });
        dialog.findViewById(R.id.min_30).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayerActivity.this, "Music will stop after 30 minutes", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                timerBtn.setImageResource(R.drawable.ic_timer_on);
                min30 = true;
                Thread newThread = new Thread(() -> {
                    try {
                        Thread.sleep((long) (30 * 60000));
                        if (min30) {
                            if (PlayerActivity.musicService != null) {
                                PlayerActivity.musicService.pause();
                            }
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                });
                newThread.start();
            }
        });
        dialog.findViewById(R.id.min_60).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayerActivity.this, "Music will stop after 60 minutes", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                timerBtn.setImageResource(R.drawable.ic_timer_on);
                min60 = true;
                Thread newThread = new Thread(() -> {
                    try {
                        Thread.sleep((long) (60 * 60000));
                        if (min60) {
                            if (PlayerActivity.musicService != null) {
                                PlayerActivity.musicService.pause();
                            }
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                newThread.start();
            }
        });
    }

    private void stopThreadBtn() {
        stopThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                timerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!min15 && !min30 && !min60)
                            showBottomSheetDialog();
                        else {
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PlayerActivity.this);
                            builder.setTitle("Stop timer")
                                    .setMessage("Do you want to stop timer?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            min15 = false;
                                            min30 = false;
                                            min60 = false;
                                            timerBtn.setImageResource(R.drawable.ic_timer);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                            AlertDialog customDialog = builder.create();
                            customDialog.show();
                            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                        }
                    }
                });
            }
        };
        stopThread.start();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        stopThreadBtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void prevThreadBtn() {
        prevThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    public void prevBtnClicked() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            }
            else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? listSongs.size() - 1 : (position - 1));
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            song_artist.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            musicService.showNotification(R.drawable.ic_pause);
            playPauseBtn.setIconResource(R.drawable.ic_pause);
//            cover_art.startAnimation(anim);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            }
            else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? listSongs.size() - 1 : (position - 1));
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            song_artist.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            playPauseBtn.setIconResource(R.drawable.ic_play);
            musicService.showNotification(R.drawable.ic_play);
//            cover_art.startAnimation(anim);
        }
    }

    private void nextThreadBtn() {
        nextThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    public void nextBtnClicked() {
        if (musicService.isPlaying()) {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            }
            else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }
            //else position will be position ...
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            song_artist.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            musicService.showNotification(R.drawable.ic_pause);
            playPauseBtn.setIconResource(R.drawable.ic_pause);
            musicService.start();
//            cover_art.startAnimation(anim);
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSongs.size() - 1);
            }
            else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            song_artist.setText(listSongs.get(position).getArtist());
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            musicService.OnCompleted();
            musicService.showNotification(R.drawable.ic_play);
            playPauseBtn.setIconResource(R.drawable.ic_play);
//            cover_art.startAnimation(anim);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();

        return random.nextInt(i + 1);
    }

    private void playThreadBtn() {
        playThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    public void playPauseBtnClicked() {
        if (musicService.isPlaying()) {
            playPauseBtn.setIconResource(R.drawable.ic_play);
            musicService.showNotification(R.drawable.ic_play);
            musicService.pause();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
//            cover_art.setAnimation(null);
        }
        else {
            playPauseBtn.setIconResource(R.drawable.ic_pause);
            musicService.showNotification(R.drawable.ic_pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
//            cover_art.startAnimation(anim);
        }
    }

    private String formattedTime(int mCurrentPosition) {

        String totalout = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalout = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalout;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        String getClass = getIntent().getStringExtra("class");
        try {
            if (getClass.equals("NowPlaying")) {
                uri = Uri.parse(listSongs.get(position).getPath());
                metaData(uri);
                song_name.setText(listSongs.get(position).getTitle());
                song_artist.setText(listSongs.get(position).getArtist());
                seekBar.setMax(musicService.getDuration() / 1000);
                if (musicService != null) {
                    if (musicService.isPlaying()) {
                        playPauseBtn.setIconResource(R.drawable.ic_play);
                    } else {
                        playPauseBtn.setIconResource(R.drawable.ic_pause);
                    }
                }
            } else if (getClass.equals("FavoriteAdapter")) {
                listSongs = favoriteSongs;
                if (listSongs != null) {
                    playPauseBtn.setIconResource(R.drawable.ic_pause);
                    uri = Uri.parse(listSongs.get(position).getPath());
                }
                Intent intent = new Intent(this, MusicService.class);
                intent.putExtra("servicePosition", position);
                startService(intent);
            } else if (getClass.equals("PlaylistDetailsAdapter")) {
                listSongs = musicPlaylist.ref.get(currentPlaylistPos).getPlaylist();
                if (listSongs != null) {
                    playPauseBtn.setIconResource(R.drawable.ic_pause);
                    uri = Uri.parse(listSongs.get(position).getPath());
                }
                Intent intent = new Intent(this, MusicService.class);
                intent.putExtra("servicePosition", position);
                startService(intent);
            }
            else {
                Log.e("Intent", "new song");
                listSongs = mFiles;
                if (listSongs != null) {
                    playPauseBtn.setIconResource(R.drawable.ic_pause);
                    uri = Uri.parse(listSongs.get(position).getPath());
                }
                Intent intent = new Intent(this, MusicService.class);
                intent.putExtra("servicePosition", position);
                startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        frag_bottom_player = findViewById(R.id.frag_bottom_player);
        timerBtn = findViewById(R.id.timerBtnPA);
        shareBtn = findViewById(R.id.shareBtnPA);
        favBtn = findViewById(R.id.favoriteBtn);
//        getSupportActionBar().hide();
    }

    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        duration_total.setText(formattedTime(durationTotal));
        try {
            byte[] art = retriever.getEmbeddedPicture();
            Bitmap bitmap;
            if (art != null) {
                bitmap = BitmapFactory.decodeByteArray(art, 0 ,art.length);
                ImageAnimation(this, cover_art, bitmap);
            } else {
                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.images)
                        .into(cover_art);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ImageAnimation(Context context, ImageView imageView, Bitmap bitmap) {
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Start animating the image
                        spinning(anim);
                        cover_art.startAnimation(anim);
                        // Later.. stop the animation
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) iBinder;
        musicService = myBinder.getService();
        musicService.setCallBack(this);

        fIndex = MusicFiles.favoriteChecker(listSongs.get(position).getId());
        Log.d("fIndex", "" + fIndex);
        if (isFavorite) {
            favBtn.setImageResource(R.drawable.ic_favorite_full);
        } else {
            favBtn.setImageResource(R.drawable.ic_favorite_border);
        }

        seekBar.setMax(musicService.getDuration() / 1000);
        metaData(uri);
        song_name.setText(listSongs.get(position).getTitle());
        song_artist.setText(listSongs.get(position).getArtist());
        musicService.OnCompleted();
        musicService.showNotification(R.drawable.ic_pause);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }

}