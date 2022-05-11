package com.example.music_player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.Model.Song;
import com.example.music_player.R;
import com.example.music_player.ServiceAPI.APIService;
import com.example.music_player.ServiceAPI.Dataservice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        ImageView imgLikes;
        public  ViewHolder (View view) {
            super(view);
            tvindex = view.findViewById(R.id.tvindexsong);
            tvslSongName = view.findViewById(R.id.tvslsongName);
            tvslSongArtist = view.findViewById(R.id.tvslsongArtist);
            imgLikes = view.findViewById(R.id.imgslLike);
            imgLikes = itemView.findViewById(R.id.imgslLike);
            imgLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgLikes.setImageResource(R.drawable.ic_favorite_border_filled);
                    Dataservice dataservice = APIService.getService();
                    Call<String> callback = dataservice.UpdateLikes("1", (String.valueOf(songs.get(getPosition()).getIdSong())));
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String likes = response.body();
                            if (likes.equals("Updated")) {
                                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                    imgLikes.setEnabled(false);
                }
            });

        }
    }
}
