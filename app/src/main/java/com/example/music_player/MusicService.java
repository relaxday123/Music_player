package com.example.music_player;

import static com.example.music_player.ApplicationClass.ACTION_EXIT;
import static com.example.music_player.ApplicationClass.ACTION_NEXT;
import static com.example.music_player.ApplicationClass.ACTION_PLAY;
import static com.example.music_player.ApplicationClass.ACTION_PREVIOUS;
import static com.example.music_player.ApplicationClass.CHANNEL_ID_2;
import static com.example.music_player.PlayerActivity.listSongs;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder mBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    Uri uri;
    int position = -1;
    ActionPlaying actionPlaying;
    MediaSessionCompat mediaSessionCompat;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String SONG_NAME = "SONG_NAME";
    public static final String SONG_ARTIST = "SONG_ARTIST";
    String nowPlayingId = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        Log.e("Bind", "Method");
        return mBinder;
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);
        String actionName = intent.getStringExtra("ActionName");
        if (myPosition != -1) {
            playMedia(myPosition);
        }
        if (actionName != null) {
            switch (actionName) {
                case "playPause":
                    Toast.makeText(this, "PlayPause", Toast.LENGTH_SHORT).show();
                    playPauseBtnClicked();
                    break;
                case "next":
                    Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                    nextBtnClicked();
                    break;
                case "previous":
                    Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
                    prevBtnClicked();
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMedia(int startPosition) {
        musicFiles = listSongs;
        position = startPosition;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    void start() {
        mediaPlayer.start();
    }

    boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    void stop() {
        mediaPlayer.stop();
    }

    void pause() {
        mediaPlayer.pause();
    }

    void release() {
        mediaPlayer.release();
    }

    int getDuration() {
        return mediaPlayer.getDuration();
    }

    void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    void createMediaPlayer(int positionInner) {
        position = positionInner;
        uri = Uri.parse(musicFiles.get(position).getPath());
        SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE)
                .edit();
        editor.putString(MUSIC_FILE, uri.toString());
        editor.putString(SONG_ARTIST, musicFiles.get(position).getArtist());
        editor.putString(SONG_NAME, musicFiles.get(position).getTitle());
        editor.apply();
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
        nowPlayingId = listSongs.get(position).getId();
    }

    void OnCompleted() {
        mediaPlayer.setOnCompletionListener(this);
        PlayerActivity.musicService.showNotification(R.drawable.ic_pause);
        PlayerActivity.playPauseBtn.setIconResource(R.drawable.ic_pause);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlaying != null) {
            actionPlaying.nextBtnClicked();
            if (mediaPlayer != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
                OnCompleted();
            }
        }
    }

    void setCallBack(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }

    void showNotification(int playPauseBtn) {
        Intent intent = new Intent(this, PlayerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,  0, intent,
                0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this,  0, prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this,  0, pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this,  0, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent exitIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_EXIT);
        PendingIntent exitPending = PendingIntent.getBroadcast(this,  0, exitIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent returnIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_EXIT);
        PendingIntent returnPending = PendingIntent.getBroadcast(this,  0, returnIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        byte[] picture = null;
        picture = getAlbumArt(musicFiles.get(position).getPath());
        Bitmap thumb = null;
        if (picture != null) {
            thumb = BitmapFactory.decodeByteArray(picture, 0 ,picture.length);
        } else {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.images);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(thumb)
                .setContentTitle(musicFiles.get(position).getTitle())
                .setContentText(musicFiles.get(position).getArtist())
                .addAction(R.drawable.ic_prev, "Previous", prevPending)
                .addAction(playPauseBtn, "Pause", pausePending)
                .addAction(R.drawable.ic_skip_next, "Next", nextPending)
                .addAction(R.drawable.ic_exit, "Exit", exitPending)
//                .ad(returnPending)
//                .setContentIntent(returnPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        startForeground(2, notification);

//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notification);
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        return art;
    }

    void nextBtnClicked() {
        if (actionPlaying != null) {
            actionPlaying.nextBtnClicked();
        }
    }

    void playPauseBtnClicked() {
        if (actionPlaying != null) {
            actionPlaying.playPauseBtnClicked();
        }
    }

    void prevBtnClicked() {
        if (actionPlaying != null) {
            actionPlaying.prevBtnClicked();
        }
    }
}
