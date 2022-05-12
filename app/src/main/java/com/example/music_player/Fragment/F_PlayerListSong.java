package com.example.music_player.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.Adapter.PlayerAdapter;
import com.example.music_player.PlayerActivity2;
import com.example.music_player.R;

public class F_PlayerListSong extends Fragment {

    View view;
    RecyclerView rvPlayerSongList;
    PlayerAdapter playerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_playerlistsong,container,false);
        rvPlayerSongList = view.findViewById(R.id.rvplayersonglist);
        if (PlayerActivity2.listsongs.size() > 0 ) {
            playerAdapter = new PlayerAdapter(getActivity(), PlayerActivity2.listsongs);
            rvPlayerSongList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvPlayerSongList.setAdapter(playerAdapter);
        }
        return view;
    }
}
