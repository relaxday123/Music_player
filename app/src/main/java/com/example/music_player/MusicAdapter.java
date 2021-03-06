package com.example.music_player;

import static com.example.music_player.PlaylistActivity.musicPlaylist;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    static ArrayList<MusicFiles> mFiles;
    boolean playlistDetails = false;
    boolean selectionActivity = false;

    MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles, Boolean playlistDetails) {
        this.mFiles = mFiles;
        this.mContext = mContext;
        this.playlistDetails = playlistDetails;
    }

    MusicAdapter(Context mContext, Boolean selectionActivity, ArrayList<MusicFiles> mFiles) {
        this.mFiles = mFiles;
        this.mContext = mContext;
        this.selectionActivity = selectionActivity;
    }

    MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles) {
        this.mFiles = mFiles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.file_name.setText(mFiles.get(holder.getAdapterPosition()).getTitle());
        byte[] image = getAlbumArt(mFiles.get(holder.getAdapterPosition()).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.album_art);
        }
        else {
            Glide.with(mContext)
                    .load(R.drawable.images)
                    .into(holder.album_art);
        }

        try {
            if (playlistDetails) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, PlayerActivity.class);
                        intent.putExtra("position", holder.getAdapterPosition());
                        intent.putExtra("class", "PlaylistDetailsAdapter");
                        ContextCompat.startActivity(mContext, intent, null);
                    }
                });
            } else if (selectionActivity) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (addSong(mFiles.get(holder.getAdapterPosition()))) {
                            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                        } else {
                            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                        }
                    }
                });
            } else {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, PlayerActivity.class);
                        intent.putExtra("position", holder.getAdapterPosition());
                        intent.putExtra("class", "CreateNewSong");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(intent);
                        ContextCompat.startActivity(mContext, intent, null);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.delete:
                            Toast.makeText(mContext, "Delete Clicked!!", Toast.LENGTH_SHORT).show();
                            deleteFile(holder.getAdapterPosition(), view);
                            break;
                    }
                    return true;
                });
            }
        });
    }

    private void deleteFile(int position, View v) {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFiles.get(position).getId())); // content://
        File file = new File(mFiles.get(position).getPath());
        Log.d("Path", mFiles.get(position).getPath());
        boolean deleted = file.delete(); // delete your file
        if (deleted) {
            mContext.getContentResolver().delete(contentUri, null, null);
            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFiles.size());
            Snackbar.make(v, "File Deleted : ", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            // may be file in sd card
            Snackbar.make(v, "Can't be deleted : ", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        ImageView album_art, menuMore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            menuMore = itemView.findViewById(R.id.menuMore);
        }
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    void updateList(ArrayList<MusicFiles> musicFilesArrayList) {
        mFiles = new ArrayList<>();
        mFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
    }

    private boolean addSong(MusicFiles musicFiles) {
        for (MusicFiles i :musicPlaylist.ref.get(PlaylistDetails.currentPlaylistPos).getPlaylist()) {
            if (musicFiles.getId().equals(i.getId())) {
                musicPlaylist.ref.get(PlaylistDetails.currentPlaylistPos).getPlaylist().remove(i);
                return false;
            }
        }
        musicPlaylist.ref.get(PlaylistDetails.currentPlaylistPos).getPlaylist().add(musicFiles);
        return true;
    }

    public void refreshPlaylist() {
        mFiles = musicPlaylist.ref.get(PlaylistDetails.currentPlaylistPos).getPlaylist();
        notifyDataSetChanged();
    }
}
