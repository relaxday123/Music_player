package com.example.music_player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.Model.Song;
import com.example.music_player.R;

import java.util.ArrayList;

public class SongListAdapter extends  RecyclerView.Adapter<SongListAdapter.ViewHolder> {

    Context context;
    ArrayList<Song>  songs;

    public SongListAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.songlist_line,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song songItem = songs.get(position);
        holder.tvindex.setText(position + 1 + "");
        holder.tvslSongName.setText(songItem.getSongName());
        holder.tvslSongArtist.setText(songItem.getArtist());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvindex,tvslSongName,tvslSongArtist;
        ImageView imgLike;
        public  ViewHolder (View view) {
            super(view);
            tvindex = view.findViewById(R.id.tvindexsong);
            tvslSongName = view.findViewById(R.id.tvslsongArtist);
            tvslSongArtist = view.findViewById(R.id.tvslsongName);
            imgLike = view.findViewById(R.id.imgslLike);

        }
    }
}
