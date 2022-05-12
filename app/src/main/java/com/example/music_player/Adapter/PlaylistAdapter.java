package com.example.music_player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.music_player.Model.Playlist;
import com.example.music_player.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends ArrayAdapter {
    public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }
    class ViewHolder{
        TextView playlistname;
        ImageView imgbackgound,imgplaylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.playlist_line,null);
            viewHolder = new ViewHolder();
            viewHolder.playlistname = convertView.findViewById(R.id.tvPlaylist);
            viewHolder.imgplaylist = convertView.findViewById(R.id.imgPlaylist);
            viewHolder.imgbackgound = convertView.findViewById(R.id.imgbgPlaylist);
            convertView.setTag(viewHolder);
        } else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        Playlist playlist = (Playlist) getItem(position);
        Picasso.with(getContext()).load(playlist.getPlaylistPic()).into(viewHolder.imgbackgound);
        Picasso.with(getContext()).load(playlist.getPlaylistIcon()).into(viewHolder.imgplaylist);
        viewHolder.playlistname.setText(playlist.getPlaylistName());
        return convertView;
    }
}
