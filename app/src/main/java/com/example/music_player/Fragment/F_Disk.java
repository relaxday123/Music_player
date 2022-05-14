package com.example.music_player.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.music_player.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class F_Disk extends Fragment {
    Context context;
    View view;
    CircleImageView circleImageView;
    TextView SongName,Artist;
    RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f_disk,container,false);
        circleImageView = view.findViewById(R.id.cover_art);
        SongName = view.findViewById(R.id.song_name);
        Artist = view.findViewById(R.id.song_artist);


        spinning(anim);
        circleImageView.startAnimation(anim);

        inflater = LayoutInflater.from(getContext());
        context = inflater.getContext();
        return view;
    }
    public void PlayNhac(String hinhanh,String songname,String artist) {
            Picasso.with(getContext()).load(hinhanh).into(circleImageView);
            SongName.setText(songname);
            Artist.setText(artist);
    }
    //675-712----73-79-----68-

    public RotateAnimation spinning(RotateAnimation anim) {
        anim.setDuration(10000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);

        return anim;
    }

}
