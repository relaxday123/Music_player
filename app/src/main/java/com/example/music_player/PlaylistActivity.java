package com.example.music_player;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.music_player.databinding.ActivityPlaylistBinding;
import com.example.music_player.databinding.AddPlaylistDialogBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PlaylistActivity extends AppCompatActivity {

    ActivityPlaylistBinding binding;
    PlaylistViewAdapter adapter;

    static MusicPlaylist musicPlaylist = new MusicPlaylist();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.linearLayout.setHasFixedSize(true);
        binding.linearLayout.setItemViewCacheSize(13);
        binding.linearLayout.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new PlaylistViewAdapter(this, PlaylistViewAdapter.playlistList = musicPlaylist.ref);
        binding.linearLayout.setAdapter(adapter);
        getSupportActionBar().hide();

        binding.addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlertDialog();
            }
        });
    }

    private void customAlertDialog() {
        View customDialog = LayoutInflater.from(this).inflate(R.layout.add_playlist_dialog, binding.getRoot(), false);
        AddPlaylistDialogBinding binder = AddPlaylistDialogBinding.bind(customDialog);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(customDialog)
                .setTitle("Playlist Details")
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        (customDialog).setVisibility(View.INVISIBLE);
                        String playlistName = binder.playlistName.getText().toString().trim();
                        String author = binder.author.getText().toString().trim();
                        if (playlistName != null && author != null)
                            if (!playlistName.isEmpty() && !author.isEmpty()) {
                                addPlaylist(playlistName, author);
                            }
                    }
                })
                .show();
    }

    private void addPlaylist(String playlistName, String author) {
        boolean playlistExists = false;
        for (Playlist i : musicPlaylist.ref) {
            if (playlistName.equals(i.getName())) {
                playlistExists = true;
                break;
            }
        }

        if (playlistExists) {
            Toast.makeText(this, "Playlist exist !!!!", Toast.LENGTH_SHORT).show();
        } else {
            Playlist tempPlaylist = new Playlist();
            tempPlaylist.setName(playlistName);
            tempPlaylist.setPlaylist(new ArrayList<MusicFiles>());
            tempPlaylist.setCreatedBy(author);
            tempPlaylist.setCreatedOn(new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
            musicPlaylist.ref.add(tempPlaylist);
            adapter.refreshPlaylist();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(SongActivity.MUSIC_LAST_PLAYED, MODE_PRIVATE);
        String path = preferences.getString(SongActivity.MUSIC_FILE, null);
        String artist = preferences.getString(SongActivity.SONG_ARTIST, null);
        String song_name = preferences.getString(SongActivity.SONG_NAME, null);
        if (path != null) {
            SongActivity.SHOW_MINI_PLAYER = true;
            SongActivity.PATH_TO_FRAG = path;
            SongActivity.ARTIST_TO_FRAG = artist;
            SongActivity.SONG_NAME_TO_FRAG = song_name;
        } else {
            SongActivity.SHOW_MINI_PLAYER = false;
            SongActivity.PATH_TO_FRAG = null;
            SongActivity.ARTIST_TO_FRAG = null;
            SongActivity.SONG_NAME_TO_FRAG = null;
        }
        adapter.notifyDataSetChanged();
    }
}