package com.example.music_player;

import static com.example.music_player.FavoriteActivity.favoriteSongs;
import static com.example.music_player.PlaylistActivity.musicPlaylist;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.music_player.Fragment.OnlineFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static boolean shuffleBoolean = false, repeatBoolean = false;

    public
    TabLayout tabLayout;
    ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_search,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();

        favoriteSongs = new ArrayList<>();
        SharedPreferences editor = getSharedPreferences("FAVORITES", MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonString = editor.getString("FavoriteSongs", null);
        Type typeToken = new TypeToken<ArrayList<MusicFiles>>(){}.getType();
        Type typeToken1 = new TypeToken<ArrayList<Playlist>>(){}.getType();
        if (jsonString != null) {
            ArrayList<MusicFiles> data = gson.fromJson(jsonString, typeToken);
            favoriteSongs.addAll(data);
        }
//        MusicPlaylist.ref = new ArrayList<Playlist>();
//        String jsonStringPlaylist = editor.getString("MusicPlaylist", null);
//        if (jsonStringPlaylist != null) {
//            ArrayList<Playlist> dataPlaylist = gson.fromJson(jsonStringPlaylist, typeToken1);
//            for (Playlist i : dataPlaylist) {
//                MusicPlaylist.ref.add(i);
//            }
//        }
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new OnlineFragment(), "Online");
        viewPagerAdapter.addFragments(new OfflineFragment(), "Offline");
        viewPagerAdapter.addFragments(new SearchFragment(),"");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(2).setIcon(tabIcons[0]);
    }



    public static class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    protected void onResume() {
         super.onResume();

        Gson gson = new Gson();
        SharedPreferences.Editor editor = getSharedPreferences("FAVORITES", MODE_PRIVATE).edit();
        String jsonString = gson.toJson(favoriteSongs);
        editor.putString("FavoriteSongs", jsonString);
        String jsonStringPlaylist = gson.toJson(musicPlaylist);
        editor.putString("MusicPlaylist", jsonStringPlaylist);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (PlayerActivity.musicService != null) {
            PlayerActivity.musicService.stopForeground(true);
            PlayerActivity.musicService.mediaPlayer.release();
            PlayerActivity.musicService = null;
        }
        System.exit(1);
    }
}