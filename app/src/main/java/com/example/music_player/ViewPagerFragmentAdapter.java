
package com.example.music_player;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music_player.Fragment.OnlineFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Offline", "Online"};

    public ViewPagerFragmentAdapter (@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new OfflineFragment();
            case 1:
                return new OnlineFragment();
        }
        return new OfflineFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}

//package com.example.music_player;
//
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
//
//    private String[] titles = new String[]{"Offline", "Online"};
//
//    public ViewPagerFragmentAdapter (@NonNull FragmentActivity fragmentActivity){
//        super(fragmentActivity);
//    }
//
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        switch (position)
//        {
//            case 0:
//                return new OfflineFragment();
//            case 1:
//                return new OnlineFragment();
//        }
//        return new OfflineFragment();
//    }
//
//    @Override
//    public int getItemCount() {
//        return titles.length;
//    }
//}

