package com.example.music_player;

import static com.example.music_player.FavoriteActivity.favoriteSongs;
import static com.example.music_player.PlaylistActivity.musicPlaylist;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    static ArrayList<MusicFiles> musicFiles;
    public static final int REQUEST_CODE = 1;
    static boolean shuffleBoolean = false, repeatBoolean = false;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static boolean SHOW_MINI_PLAYER = false;
    public static String PATH_TO_FRAG = null;
    public static String ARTIST_TO_FRAG = null;
    public static String SONG_NAME_TO_FRAG = null;
    public static final String SONG_NAME = "SONG_NAME";
    public static final String SONG_ARTIST = "SONG_ARTIST";
    private String MY_SORT_PREF = "SortOrder";

    public
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();

        favoriteSongs = new ArrayList<>();
        SharedPreferences editor = getSharedPreferences("FAVORITES", MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonString = editor.getString("FavoriteSongs", null);
        Type typeToken = new TypeToken<ArrayList<MusicFiles>>(){}.getType();
        Type typeToken1 = new TypeToken<ArrayList<MusicPlaylist>>(){}.getType();
        if (jsonString != null) {
            ArrayList<MusicFiles> data = gson.fromJson(jsonString, typeToken);
            favoriteSongs.addAll(data);
        }
        musicPlaylist = new MusicPlaylist();
        String jsonStringPlaylist = editor.getString("MusicPlaylist", null);
        Log.d("playlist", jsonStringPlaylist);
        if (jsonStringPlaylist != null) {
            ArrayList<MusicPlaylist> dataPlaylist = gson.fromJson(jsonStringPlaylist, typeToken1);
//            musicPlaylist = dataPlaylist;
//            for (MusicPlaylist.ref i : dataPlaylist.ref) {
//                musicPlaylist.ref = i;
//            }
        }
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , REQUEST_CODE);
        }
        else {
            musicFiles = getAllAudio(this);
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Do whatever you want permission related
                musicFiles = getAllAudio(this);
                initViewPager();
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);
            }
        }
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new OnlineFragment(), "Online");
        viewPagerAdapter.addFragments(new OfflineFragment(), "Offline");
//        viewPagerAdapter.addFragments(new PlaylistFragment(), "Playlist");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.titlebar_logo, null);
//        getSupportActionBar().setCustomView(view);
//        getSupportActionBar().hide();
//        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this);
//        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();
    }

    public ArrayList<MusicFiles> getAllAudio(Context context) {
        SharedPreferences preferences = getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE);
        String sortOrder = preferences.getString("sorting", "sortByName");
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        String order = null;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        switch (sortOrder) {
            case "sortByName":
                order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
                break;
            case "sortByDate":
                order = MediaStore.MediaColumns.DATE_ADDED + " ASC";
                break;
            case "sortBySize":
                order = MediaStore.MediaColumns.SIZE + " DESC";
                break;
        }
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, order);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                String id = cursor.getString(5);

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration, id);
                // take log.e for check
                Log.e("Path : " + path, "Album : " + album);
                if (tempAudioList.isEmpty())
                    tempAudioList.add(musicFiles);
                else if (!musicFiles.getTitle().equals(tempAudioList.get(tempAudioList.size()-1).getTitle()))
                    tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search, menu);
//        MenuItem menuItem = menu.findItem(R.id.search_option);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setOnQueryTextListener(this);
//
//
//        getMenuInflater().inflate(R.menu.more_option, menu);
//        MenuItem loginItem = menu.findItem(R.id.loginBtn);
//        MenuItem registerItem = menu.findItem(R.id.registerBtn);
//        MenuItem logoutItem = menu.findItem(R.id.logoutBtn);
//        MenuItem aboutItem = menu.findItem(R.id.aboutBtn);
//
//        loginItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                return true;
//            }
//        });
//
//        registerItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//                return true;
//            }
//        });
//
//        logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    FirebaseAuth.getInstance().signOut();
//                    Toast.makeText(MainActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    // No user is signed in
//                    Toast.makeText(MainActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//        });
//
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<MusicFiles> myFiles = new ArrayList<>();
        for (MusicFiles song : musicFiles) {
            if (song.getTitle().toLowerCase().contains(userInput)) {
                myFiles.add(song);
            }
        }
//        OfflineFragment.musicAdapter.updateList(myFiles);
        return true;
    }

    @Override
    protected void onResume() {
//         super.onResume();
//         SharedPreferences preferences = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
//         String path = preferences.getString(MUSIC_FILE, null);
//         String artist = preferences.getString(SONG_ARTIST, null);
//         String song_name = preferences.getString(SONG_NAME, null);
//         if (path != null) {
//             SHOW_MINI_PLAYER = true;
//             PATH_TO_FRAG = path;
//             ARTIST_TO_FRAG = artist;
//             SONG_NAME_TO_FRAG = song_name;
//         } else {
//             SHOW_MINI_PLAYER = false;
//             PATH_TO_FRAG = null;
//             ARTIST_TO_FRAG = null;
//             SONG_NAME_TO_FRAG = null;
//         }

        Gson gson = new Gson();
        SharedPreferences.Editor editor = getSharedPreferences("FAVORITES", MODE_PRIVATE).edit();
        String jsonString = gson.toJson(favoriteSongs);
        editor.putString("FavoriteSongs", jsonString);
        String jsonStringPlaylist = gson.toJson(musicPlaylist);
        editor.putString("MusicPlaylist", jsonStringPlaylist);
        editor.apply();
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences preferences = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
//        String path = preferences.getString(MUSIC_FILE, null);
//        String artist = preferences.getString(SONG_ARTIST, null);
//        String song_name = preferences.getString(SONG_NAME, null);
//        if (path != null) {
//            SHOW_MINI_PLAYER = true;
//            PATH_TO_FRAG = path;
//            ARTIST_TO_FRAG = artist;
//            SONG_NAME_TO_FRAG = song_name;
//        } else {
//            SHOW_MINI_PLAYER = false;
//            PATH_TO_FRAG = null;
//            ARTIST_TO_FRAG = null;
//            SONG_NAME_TO_FRAG = null;
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE).edit();
        switch (item.getItemId()) {
            case R.id.by_name:
                editor.putString("sorting",  "sortByName");
                editor.apply();
                this.recreate();
                break;
            case R.id.by_date:
                editor.putString("sorting",  "sortByDate");
                editor.apply();
                this.recreate();
                break;
            case R.id.by_size:
                editor.putString("sorting",  "sortBySize");
                editor.apply();
                this.recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
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