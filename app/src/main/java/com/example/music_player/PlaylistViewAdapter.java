package com.example.music_player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.databinding.PlaylistViewBinding;

import java.util.ArrayList;

public class PlaylistViewAdapter extends RecyclerView.Adapter<PlaylistViewAdapter.MyHolder> {

    private Context pContext;
    static ArrayList<MusicFiles> playlistList;

    PlaylistViewAdapter(Context mContext, ArrayList<MusicFiles> playlistList) {
        this.playlistList = playlistList;
        this.pContext = mContext;
    }

//    @Override
//    public void onBindViewHolder(@NonNull PlaylistViewAdapter holder, int position) {
//
//    }

    @NonNull
    @Override
    public PlaylistViewAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(pContext).inflate(R.layout.music_items, parent, false);
        return new PlaylistViewAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewAdapter.MyHolder holder, int position) {
        holder.file_name.setText(playlistList.get(holder.getAdapterPosition()).getTitle());
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        ImageView album_art, menuMore;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            menuMore = itemView.findViewById(R.id.menuMore);
        }
    }
}
