package com.example.music_player;

import static com.example.music_player.PlaylistViewAdapter.playlistList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_player.databinding.AddPlaylistDialogBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
        addPlaylist = view.findViewById(R.id.addPlaylist);
        playlistView.setHasFixedSize(true);
//        tempList.add(0,new MusicFiles.Playlist());
//        tempList.add("Travel Song");
//        tempList.add("Working Song");
//        tempList.add("Relax Song");
//        tempList.add("Train Song");
//        tempList.add("cc Song");
        playlistAdapter = new PlaylistViewAdapter(getContext(), playlistList = MusicPlaylist.ref);
        playlistView.setAdapter(playlistAdapter);
        playlistView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlertDialog();
            }
        });

        return view;
    }

    private void customAlertDialog() {
        View customDialog = LayoutInflater.from(getContext()).inflate(R.layout.add_playlist_dialog, playlistView, false);
        AddPlaylistDialogBinding binder = AddPlaylistDialogBinding.bind(customDialog);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setView(customDialog)
                .setTitle("Playlist Details")
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        (customDialog).setVisibility(View.INVISIBLE);
                        String playlistName = binder.playlistName.getText().toString().trim();
                        String author = binder.author.getText().toString().trim();
                        if (playlistName != null && author != null)
                            if (!playlistName.isEmpty() && !author.isEmpty()) {
                                addPlaylist(playlistName, author);
                            }
                    }
                })
                .show();
    }

    private void addPlaylist(String playlistName, String author) {
        boolean playlistExists = false;
        for (Playlist i : MusicPlaylist.ref) {
            if (playlistName.equals(i.getName())) {
                playlistExists = true;
                break;
            }
        }

        if (playlistExists) {
            Toast.makeText(getContext(), "Playlist exist !!!!", Toast.LENGTH_SHORT).show();
        } else {
            Playlist tempPlaylist = new Playlist();
            tempPlaylist.setName(playlistName);
            tempPlaylist.setPlaylist(new ArrayList<MusicFiles>());
            tempPlaylist.setCreatedBy(author);
            tempPlaylist.setCreatedOn(new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
            MusicPlaylist.ref.add(tempPlaylist);
            playlistAdapter.refreshPlaylist();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        playlistAdapter.notifyDataSetChanged();
    }
}