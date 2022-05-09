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

import com.example.music_player.Adapter.RandomSongAdapter;
import com.example.music_player.Model.Song;
import com.example.music_player.R;
import com.example.music_player.ServiceAPI.APIService;
import com.example.music_player.ServiceAPI.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class F_RandomSong extends Fragment {

    View view;
    RecyclerView recyclerViewrdSong;
    RandomSongAdapter randomSongAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_randomsong,container,false);
        recyclerViewrdSong = view.findViewById(R.id.rvrandomSong);
        GetData();
        return view;
    }

    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Song>> callback = dataservice.GetRandomSong();
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                ArrayList<Song> rdSong = (ArrayList<Song>) response.body();
                randomSongAdapter = new RandomSongAdapter(getActivity(),rdSong);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewrdSong.setLayoutManager(linearLayoutManager);
                recyclerViewrdSong.setAdapter(randomSongAdapter);
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });
    }
}
