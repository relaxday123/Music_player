package com.example.music_player;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class OfflineFragment extends Fragment {

    Button song_button, playlist_button;

    public OfflineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offline, container, false);
        song_button = view.findViewById(R.id.song_button);
        playlist_button = view.findViewById(R.id.playlist_button);


        song_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SongActivity.class);
                startActivity(intent);
            }
        });

        playlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlaylistActivity.class);
                startActivity(intent);
            }
        });
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        if (!(musicFiles.size() < 1)) {
//            musicAdapter = new MusicAdapter(getContext(), musicFiles);
//            recyclerView.setAdapter(musicAdapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
//                    false));
//        }
        return view;
    }
}