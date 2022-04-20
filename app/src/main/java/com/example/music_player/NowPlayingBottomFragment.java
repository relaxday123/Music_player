package com.example.music_player;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NowPlayingBottomFragment extends Fragment {

    ImageView nextBtn, albumArt, prevBtn;
    TextView artist, songName;
    FloatingActionButton playPauseBtn;
    View view;

    public NowPlayingBottomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_now_playing_bottom, container, false);
        artist = view.findViewById(R.id.song_artist_miniPlayer);
        songName = view.findViewById(R.id.song_name_miniPlayer);
        albumArt = view.findViewById(R.id.bottom_album_art);
        nextBtn = view.findViewById(R.id.skip_next_button);
        playPauseBtn = view.findViewById(R.id.play_pause_miniPlayer);
        prevBtn = view.findViewById(R.id.skip_previous_button);

        return view;
    }
}