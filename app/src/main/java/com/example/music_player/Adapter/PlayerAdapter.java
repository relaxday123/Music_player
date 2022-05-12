package com.example.music_player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.Model.Song;
import com.example.music_player.R;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> songs;

    public PlayerAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.player_line,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.artist.setText(song.getArtist());
        holder.index.setText(position + 1 + "");
        holder.songName.setText(song.getSongName());

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        TextView artist,songName,index;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artist = itemView.findViewById(R.id.tvPlayersongArtist);
            songName = itemView.findViewById(R.id.tvPlayersongName);
            index = itemView.findViewById(R.id.tvPlayerindexsong);
        }
    }
}
