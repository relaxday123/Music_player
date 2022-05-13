package com.example.music_player;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_player.databinding.ActivityPlaylistDetailsBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PlaylistDetails extends AppCompatActivity {
    ActivityPlaylistDetailsBinding binding;
    static MusicAdapter adapter;
    static int currentPlaylistPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaylistDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentPlaylistPos = getIntent().getIntExtra("index", -1);

//        try {
//            MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist() =
//                checkPlaylist(playlist = PlaylistActivity.musicPlaylist.ref[currentPlaylistPos].playlist)}
//        catch(e: Exception){}

        binding.playlistDetailsRV.setItemViewCacheSize(10);
        binding.playlistDetailsRV.setHasFixedSize(true);
        binding.playlistDetailsRV.setLayoutManager(new LinearLayoutManager(this));
//        MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist().addAll(MainActivity.musicFiles);
        adapter = new MusicAdapter(this, MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist(), true);
        binding.playlistDetailsRV.setAdapter(adapter);
//        getSupportActionBar().hide();
        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.addBtnPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlaylistDetails.this, SelectionActivity.class));
            }
        });
        binding.removeAllPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(PlaylistDetails.this);
                builder.setTitle("Remove")
                        .setMessage("Do you want to remove all songs from playlist?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist().clear();
                                adapter.refreshPlaylist();
                                if (PlayerActivity.musicService != null) {
                                    PlayerActivity.musicService.stopForeground(true);
                                    PlayerActivity.musicService.mediaPlayer.release();
                                    PlayerActivity.musicService = null;
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                AlertDialog customDialog = builder.create();
                customDialog.show();
            }
        });
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onResume() {
        super.onResume();
        adapter.refreshPlaylist();
        adapter.notifyDataSetChanged();
        binding.playlistNamePD.setText(MusicPlaylist.ref.get(currentPlaylistPos).getName());
        binding.moreInfoPD.setText("Total " + adapter.getItemCount() + " songs.\n\n" +
                "Created on:\n" + MusicPlaylist.ref.get(currentPlaylistPos).getCreatedOn() + "\n\n" +
                MusicPlaylist.ref.get(currentPlaylistPos).getCreatedBy());
        try {
            if (adapter.getItemCount() > 0) {
                Glide.with(this)
                        .load(MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist().get(0).getPath())
                        .apply(RequestOptions.placeholderOf(R.drawable.images).centerCrop())
                        .into(binding.playlistImgPD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}