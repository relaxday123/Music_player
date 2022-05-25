package com.example.music_player.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.music_player.Model.Mymodel;
import com.example.music_player.R;

import java.util.ArrayList;

public class MyAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Mymodel> mymodelArrayList;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, ArrayList<Mymodel> mymodelArrayList) {
        this.context = context;
        this.mymodelArrayList = mymodelArrayList;
    }

    @Override
    public int getCount() {
        return mymodelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int poistion) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.card_item, container, false);

        //
        ImageView bannerTV = view.findViewById(R.id.bannerTV);
        TextView titleTV = view.findViewById(R.id.titleTV);
        TextView descriptionTV = view.findViewById(R.id.descriptionTV);

        // get data
        Mymodel mymodel = mymodelArrayList.get(poistion);
        String title = mymodel.getTitle();
        String description = mymodel.getDescription();
        int image = mymodel.getImage();

        // set data
        bannerTV.setImageResource(image);
        descriptionTV.setText(description);
        titleTV.setText(title);

        //handle card click

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, title + "\n" + description + "\n", Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(view, poistion);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}