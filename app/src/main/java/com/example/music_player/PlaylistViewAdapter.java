package com.example.music_player;

import static com.example.music_player.PlaylistActivity.musicPlaylist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class PlaylistViewAdapter extends RecyclerView.Adapter<PlaylistViewAdapter.MyHolder> {

    private Context pContext;
    static ArrayList<Playlist> playlistList;
    ImageButton playlistDelBtn;

    PlaylistViewAdapter(Context mContext, ArrayList<Playlist> playlistList) {
        this.playlistList = playlistList;
        this.pContext = mContext;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(pContext).inflate(R.layout.playlist_view, parent, false);
        playlistDelBtn = view.findViewById(R.id.playlistDelBtn);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewAdapter.MyHolder holder, int position) {
        holder.file_name.setText(playlistList.get(position).getName());
        holder.file_name.isSelected();

        try {
            playlistDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(pContext);
                    builder.setTitle(playlistList.get(holder.getAdapterPosition()).getName())
                            .setMessage("Do you want to delete playlist?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    musicPlaylist.ref.remove(holder.getAdapterPosition());
                                    refreshPlaylist();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(pContext, PlaylistDetails.class);
                intent.putExtra("index", holder.getAdapterPosition());
                ContextCompat.startActivity(pContext, intent, null);
            }
        });

        if (musicPlaylist.ref.get(position).getPlaylist().size() > 0) {
            Glide.with(pContext)
                    .load(getAlbumArt(musicPlaylist.ref.get(position).getPlaylist().get(0).getPath()))
                    .apply(RequestOptions.placeholderOf(R.drawable.images).centerCrop())
                    .into(holder.album_art);
        }
    }

    @Override
    public int getItemCount() {
        if (playlistList == null)
            return 0;
        else
            return playlistList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        ImageView album_art;
        ImageButton delBtn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.playlistName);
            album_art = itemView.findViewById(R.id.playlistImg);
            delBtn = itemView.findViewById(R.id.playlistDelBtn);
        }
    }

    public void refreshPlaylist() {
        playlistList = new ArrayList<>();
        playlistList.addAll(musicPlaylist.ref);
        notifyDataSetChanged();
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        return art;
    }
}
