package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;

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
        binding.playlistDetailsRV.setItemViewCacheSize(10);
        binding.playlistDetailsRV.setHasFixedSize(true);
        binding.playlistDetailsRV.setLayoutManager(new LinearLayoutManager(this));
//        MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist().addAll(MainActivity.musicFiles);
        adapter = new MusicAdapter(this, MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist(), true);
        binding.playlistDetailsRV.setAdapter(adapter);
        getSupportActionBar().hide();
        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.addBtnPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SelectionActivity.class));
            }
        });
        binding.removeAllPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getApplicationContext());
                builder.setTitle("Remove")
                        .setMessage("Do you want to remove all songs from playlist?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist().clear();
                                adapter.refreshPlaylist();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.playlistNamePD.setText(MusicPlaylist.ref.get(currentPlaylistPos).getName());
        binding.moreInfoPD.setText("Total " + adapter.getItemCount() + "songs.\n\n" +
                "Created on:\n" + MusicPlaylist.ref.get(currentPlaylistPos).getCreatedOn() + "\n\n" +
                MusicPlaylist.ref.get(currentPlaylistPos).getCreatedBy());
        try {
            if (adapter.getItemCount() > 0) {
                byte[] picture = null;
                picture = getAlbumArt(MusicPlaylist.ref.get(currentPlaylistPos).getPlaylist().get(0).getPath());
                Bitmap thumb = null;
                if (picture != null) {
                    thumb = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                } else {
                    thumb = BitmapFactory.decodeResource(getResources(), R.drawable.images);
                }
                Glide.with(this)
                        .load(thumb)
                        .into(binding.playlistImgPD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        return art;
    }
}