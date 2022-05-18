package com.example.music_player;
import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.music_player.Adapter.MyAdapter;
import com.example.music_player.Model.Mymodel;

import java.util.ArrayList;

public class OfflineFragment extends Fragment {

    Button song_button, playlist_button;
    ImageButton menu_button;

    //Action bar
    private ActionBar actionBar;

    // ui views
    private ViewPager viewPager;
    public OfflineFragment() {
    }

    private ArrayList<Mymodel> mymodelArrayList;
    private MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offline, container, false);
        song_button = view.findViewById(R.id.song_button);
        playlist_button = view.findViewById(R.id.playlist_button);

        menu_button = view.findViewById(R.id.menu_button);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

//        init UI views
        viewPager = view.findViewById(R.id.viewPager);
//        viewPager.setAdapter(myAdapter);
        loadCards();

      //  set viewpaper change listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String title = mymodelArrayList.get(position).getTitle();
//                actionBar.setTitle(title);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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
        //menu button
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
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

    private void loadCards() {

        //init list
        mymodelArrayList = new ArrayList<Mymodel>();

        //add item to list
        mymodelArrayList.add(new Mymodel(
                "Singer 1",
                "description 1",
                R.drawable.aururasinger));
        mymodelArrayList.add(new Mymodel(
                "Singer 2",
                "description 2",
                R.drawable.aururasinger));
        mymodelArrayList.add(new Mymodel(
                "Singer 3",
                "description 3",
                R.drawable.aururasinger));
        mymodelArrayList.add(new Mymodel(
                "Singer 4",
                "description 4",
                R.drawable.aururasinger));

        //setup adapter
        myAdapter = new MyAdapter(getContext(), mymodelArrayList);
        //set adapter to view paper
        viewPager.setAdapter(myAdapter);

        viewPager.setPadding(100, 0, 100, 0);
    }
}