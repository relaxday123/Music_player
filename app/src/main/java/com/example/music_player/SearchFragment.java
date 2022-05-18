package com.example.music_player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.Adapter.AlbumAdapter;
import com.example.music_player.Adapter.SearchAdapter;
import com.example.music_player.Model.Album;
import com.example.music_player.Model.Song;
import com.example.music_player.ServiceAPI.APIService;
import com.example.music_player.ServiceAPI.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    View view;
    Toolbar toolbar;
    RecyclerView recyclerViewSong,recyclerViewAlbum;
    SearchAdapter searchAdapter;
    AlbumAdapter albumAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_search,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        recyclerViewSong = view.findViewById(R.id.RvsSongs);
        recyclerViewAlbum = view.findViewById(R.id.RvSAlbum);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.online_search,menu);
        MenuItem menuItem = menu.findItem(R.id.online_search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchSong(newText);
                SearchAlbum(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void SearchSong(String Key){
        Dataservice dataservice = APIService.getService();
        Call<List<Song>> callback = dataservice.SearchSong(Key);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                ArrayList<Song> songs = (ArrayList<Song>) response.body();
                if (songs.size() > 0) {
                    searchAdapter = new SearchAdapter(getActivity(),songs);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerViewSong.setLayoutManager(linearLayoutManager);
                    recyclerViewSong.setAdapter(searchAdapter);
                    recyclerViewSong.setVisibility(view.VISIBLE);
                } else {
                    recyclerViewSong.setVisibility(view.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });
    }

    private void SearchAlbum(String Key){
        Dataservice dataservice = APIService.getService();
        Call<List<Album>> callback = dataservice.SearchAlbum(Key);
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> albums = (ArrayList<Album>)  response.body();
                if (albums.size() > 0) {
                    albumAdapter = new AlbumAdapter(getActivity(),albums);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    recyclerViewAlbum.setLayoutManager(linearLayoutManager);
                    recyclerViewAlbum.setAdapter(albumAdapter);
                    recyclerViewAlbum.setVisibility(view.VISIBLE);
                } else {
                    recyclerViewAlbum.setVisibility(view.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }

}
