package com.example.music_player;

import static com.example.music_player.FavoriteActivity.favoriteSongs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private Context fContext;
    static ArrayList<MusicFiles> fFiles;

    FavoriteAdapter(Context mContext, ArrayList<MusicFiles> mFiles) {
        this.fFiles = mFiles;
        this.fContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fContext).inflate(R.layout.favorite_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(fFiles.get(position).getTitle());
        Glide.with(fContext)
                .load(favoriteSongs.get(position))
                .apply(RequestOptions.placeholderOf(R.drawable.images).centerCrop())
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fContext, PlayerActivity.class);
                intent.putExtra("position", holder.getAdapterPosition());
                intent.putExtra("class", "FavoriteAdapter");
                ContextCompat.startActivity(fContext, intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.songNameFV);
            image = itemView.findViewById(R.id.songImgFV);
        }
    }
}
