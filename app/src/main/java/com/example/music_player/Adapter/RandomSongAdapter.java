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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RandomSongAdapter extends RecyclerView.Adapter<RandomSongAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> rdsong;

    public RandomSongAdapter(Context context, ArrayList<Song> rdsong) {
        this.context = context;
        this.rdsong = rdsong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.randomsong_line,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = rdsong.get(position);
        holder.rdsongName.setText(song.getSongName());
        holder.rdsongArtist.setText(song.getArtist());
        Picasso.with(context).load(song.getSongPic()).into(holder.imgSong);
    }

    @Override
    public int getItemCount() {
        return rdsong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSong;
        TextView rdsongName,rdsongArtist;
        public  ViewHolder(View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.imgrdSong);
            rdsongName = itemView.findViewById(R.id.tvrdSongName);
            rdsongArtist = itemView.findViewById(R.id.tvrdSongArtist);

        }
    }
}
