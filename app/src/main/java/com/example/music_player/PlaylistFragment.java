package com.example.music_player;

import static com.example.music_player.MainActivity.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class PlaylistFragment extends Fragment {

    RecyclerView playlistView;
    static PlaylistViewAdapter playlistAdapter;
    ExtendedFloatingActionButton addPlaylist;

    public PlaylistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        playlistView = view.findViewById(R.id.linearLayout);
        playlistView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            playlistAdapter = new PlaylistViewAdapter(getContext(), musicFiles);
            playlistView.setAdapter(playlistAdapter);
            playlistView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                    false));
        }
//        addPlaylist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
        return view;
    }

//    private void customAlertDialog() {
//        LinearLayout ll = (LinearLayout) inflate(context, R.layout.amount_widget, this);
//    }
}