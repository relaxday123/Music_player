package com.example.music_player;

import static com.example.music_player.ApplicationClass.ACTION_EXIT;
import static com.example.music_player.ApplicationClass.ACTION_NEXT;
import static com.example.music_player.ApplicationClass.ACTION_PLAY;
import static com.example.music_player.ApplicationClass.ACTION_PREVIOUS;
import static com.example.music_player.ApplicationClass.ACTION_RETURN;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent serviceIntent = new Intent(context, MusicService.class);
        if (actionName != null) {
            switch (actionName) {
                case ACTION_PLAY:
                    serviceIntent.putExtra("ActionName", "playPause");
                    context.startService(serviceIntent);
                    break;
                case ACTION_NEXT:
                    serviceIntent.putExtra("ActionName", "next");
                    context.startService(serviceIntent);
                    break;
                case ACTION_PREVIOUS:
                    serviceIntent.putExtra("ActionName", "previous");
                    context.startService(serviceIntent);
                    break;
                case ACTION_EXIT:
                    PlayerActivity.musicService.stopForeground(true);
                    PlayerActivity.musicService.mediaPlayer.release();
                    PlayerActivity.musicService = null;
                    break;
                case ACTION_RETURN:
                    intent.putExtra("position", PlayerActivity.position);
                    intent.putExtra("class", "NowPlaying");
                    ContextCompat.startActivity(context, intent, null);
                    break;
            }
        }
    }
}
