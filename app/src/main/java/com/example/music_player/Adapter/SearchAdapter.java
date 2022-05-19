package com.example.music_player.Adapter;

import static com.example.music_player.PlayerActivity2.mediaPlayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.Model.Song;
import com.example.music_player.PlayerActivity2;
import com.example.music_player.R;
import com.example.music_player.ServiceAPI.APIService;
import com.example.music_player.ServiceAPI.Dataservice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> rdsong;

    public SearchAdapter(Context context, ArrayList<Song> rdsong) {
        this.context = context;
        this.rdsong = rdsong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.randomsong_line, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSong, imgLikes;
        TextView rdsongName, rdsongArtist;

        public ViewHolder(View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.imgrdSong);
            rdsongName = itemView.findViewById(R.id.tvrdSongName);
            rdsongArtist = itemView.findViewById(R.id.tvrdSongArtist);
            imgLikes = itemView.findViewById(R.id.imgLove);
            imgLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgLikes.setImageResource(R.drawable.ic_favorite_border_filled);
                    Dataservice dataservice = APIService.getService();
                    Call<String> callback = dataservice.UpdateLikes("1", (String.valueOf(rdsong.get(getPosition()).getIdSong())));
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayerActivity2.class);
                    intent.putExtra("Song",rdsong.get(getPosition()));
                    context.startActivity(intent);
                    if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                }
            });
        }
    }
}
