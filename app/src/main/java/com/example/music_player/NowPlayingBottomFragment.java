package com.example.music_player;

import static android.content.Context.MODE_PRIVATE;
import static com.example.music_player.SongActivity.ARTIST_TO_FRAG;
import static com.example.music_player.SongActivity.PATH_TO_FRAG;
import static com.example.music_player.SongActivity.SHOW_MINI_PLAYER;
import static com.example.music_player.SongActivity.SONG_NAME_TO_FRAG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.music_player.databinding.FragmentNowPlayingBottomBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NowPlayingBottomFragment extends Fragment implements ServiceConnection {

    ImageView nextBtn, albumArt, prevBtn;
    TextView artist, songName;
    public static FloatingActionButton playPauseBtn;
    View view;
    MusicService musicService;
    RelativeLayout card_bottom_player;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String SONG_NAME = "SONG_NAME";
    public static final String SONG_ARTIST = "SONG_ARTIST";
    FragmentNowPlayingBottomBinding binding;

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
        card_bottom_player = view.findViewById(R.id.card_bottom_player);

        songName.setSelected(true);
        artist.setSelected(true);

//        binding.getRoot().setVisibility(View.INVISIBLE);
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(this);
        ft.commit();

        try {
            nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService != null) {
                    musicService.nextBtnClicked();
                    if (getActivity() != null) {
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE)
                                .edit();
                        editor.putString(MUSIC_FILE, musicService.musicFiles.get(musicService.position).getPath());
                        editor.putString(SONG_ARTIST, musicService.musicFiles.get(musicService.position).getArtist());
                        editor.putString(SONG_NAME, musicService.musicFiles.get(musicService.position).getTitle());
                        editor.apply();
                        SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
                        String path = preferences.getString(MUSIC_FILE, null);
                        String artistName = preferences.getString(SONG_ARTIST, null);
                        String song_name = preferences.getString(SONG_NAME, null);
                        if (path != null) {
                            SHOW_MINI_PLAYER = true;
                            PATH_TO_FRAG = path;
                            ARTIST_TO_FRAG = artistName;
                            SONG_NAME_TO_FRAG = song_name;
                        } else {
                            SHOW_MINI_PLAYER = false;
                            PATH_TO_FRAG = null;
                            ARTIST_TO_FRAG = null;
                            SONG_NAME_TO_FRAG = null;
                        }
                        if (SHOW_MINI_PLAYER) {
                            if (PATH_TO_FRAG != null) {
                                byte[] art = getAlbumArt(PATH_TO_FRAG);
                                if (art != null) {
                                    Glide.with(getContext()).load(art)
                                            .into(albumArt);
                                } else {
                                    Glide.with(getContext()).load(R.drawable.images)
                                            .into(albumArt);
                                }
                                songName.setText(SONG_NAME_TO_FRAG);
                                artist.setText(ARTIST_TO_FRAG);
                            }
                        }
                    }
                }
            }
        });

            playPauseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (musicService != null) {
                        musicService.playPauseBtnClicked();
                        if (musicService.isPlaying()) {
                            playPauseBtn.setImageResource(R.drawable.ic_pause);
                        } else {
                            playPauseBtn.setImageResource(R.drawable.ic_play);
                        }
                    }
                }
            });

            prevBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (musicService != null) {
                        musicService.prevBtnClicked();
                        if (getActivity() != null) {
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE)
                                    .edit();
                            editor.putString(MUSIC_FILE, musicService.musicFiles.get(musicService.position).getPath());
                            editor.putString(SONG_ARTIST, musicService.musicFiles.get(musicService.position).getArtist());
                            editor.putString(SONG_NAME, musicService.musicFiles.get(musicService.position).getTitle());
                            editor.apply();
                            SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
                            String path = preferences.getString(MUSIC_FILE, null);
                            String artistName = preferences.getString(SONG_ARTIST, null);
                            String song_name = preferences.getString(SONG_NAME, null);
                            if (path != null) {
                                SHOW_MINI_PLAYER = true;
                                PATH_TO_FRAG = path;
                                ARTIST_TO_FRAG = artistName;
                                SONG_NAME_TO_FRAG = song_name;
                            } else {
                                SHOW_MINI_PLAYER = false;
                                PATH_TO_FRAG = null;
                                ARTIST_TO_FRAG = null;
                                SONG_NAME_TO_FRAG = null;
                            }
                            if (SHOW_MINI_PLAYER) {
                                if (PATH_TO_FRAG != null) {
                                    byte[] art = getAlbumArt(PATH_TO_FRAG);
                                    if (art != null) {
                                        Glide.with(getContext()).load(art)
                                                .into(albumArt);
                                    } else {
                                        Glide.with(getContext()).load(R.drawable.images)
                                                .into(albumArt);
                                    }
                                    songName.setText(SONG_NAME_TO_FRAG);
                                    artist.setText(ARTIST_TO_FRAG);
                                }
                            }
                        }
                    }
                }
            });

            card_bottom_player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(requireContext(), PlayerActivity.class);
                    intent.putExtra("position", PlayerActivity.position);
                    intent.putExtra("class", "NowPlaying");
                    ContextCompat.startActivity(requireContext(), intent, null);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(getContext(), MusicService.class);
        if (getContext() != null) {
            getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (PlayerActivity.musicService != null) {
            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.show(this);
            ft.commit();
            if (PlayerActivity.musicService.isPlaying()) {
                playPauseBtn.setImageResource(R.drawable.ic_pause);
            } else {
                playPauseBtn.setImageResource(R.drawable.ic_play);
            }
        }

        if (PlayerActivity.musicService == null) {
            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(this);
            ft.commit();
        }

        if (SHOW_MINI_PLAYER) {
            if (PATH_TO_FRAG != null) {
                byte[] art = getAlbumArt(PATH_TO_FRAG);
                if (art != null) {
                    Glide.with(getContext()).load(art)
                            .into(albumArt);
                } else {
                    Glide.with(getContext()).load(R.drawable.images)
                            .into(albumArt);
                }
                songName.setText(SONG_NAME_TO_FRAG);
                artist.setText(ARTIST_TO_FRAG);
                Intent intent = new Intent(getContext(), MusicService.class);
                if (getContext() != null) {
                    getContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getContext() != null) {
            getContext().unbindService(this);
        }
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        return art;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) iBinder;
        musicService = myBinder.getService();
//        musicService.OnCompleted();
//        getView().setVisibility(View.VISIBLE);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
//        getView().setVisibility(View.INVISIBLE);
    }
}