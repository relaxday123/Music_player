package com.example.music_player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.music_player.Model.Theme;
import com.example.music_player.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {
    ArrayList<Theme> arrayListbanner;
    Context context;


    public BannerAdapter(Context context, ArrayList<Theme> arrayListbanner){
        this.context = context;
        this.arrayListbanner = arrayListbanner;
    }

    @Override
    public int getCount() {
        return arrayListbanner.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.banner_line,null);

        ImageView imgbgBanner = view.findViewById(R.id.imgbgBanner);
        ImageView imgBanner = view.findViewById(R.id.imgbn1);

        Picasso.with(context).load(arrayListbanner.get(position).getThemePic()).into(imgbgBanner);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
