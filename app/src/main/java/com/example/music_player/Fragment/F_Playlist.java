package com.example.music_player.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.music_player.Adapter.PlaylistAdapter;
import com.example.music_player.Model.Playlist;
import com.example.music_player.R;
import com.example.music_player.ServiceAPI.APIService;
import com.example.music_player.ServiceAPI.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class F_Playlist extends Fragment {
    View view;
    ListView lvplaylist;
    PlaylistAdapter playlistAdapter;
    ArrayList<Playlist> playlists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_playlist,container,false);
        lvplaylist = view.findViewById(R.id.listviewplaylist);
        GetData();
        return view;
    }

    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Playlist>> callback = dataservice.GetPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> playlists = (ArrayList<Playlist>) response.body();
                playlistAdapter = new PlaylistAdapter(getActivity(), android.R.layout.simple_list_item_1,playlists);
                lvplaylist.setAdapter(playlistAdapter);
                }
            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
            }
        });
    }
}
