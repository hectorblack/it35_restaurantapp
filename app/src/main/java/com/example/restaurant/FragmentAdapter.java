package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {


    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) { //getItem

        switch(position){
            case 0:
                return new fragmentReserveTables();

            case 1:
                return new fragmentFoodList();

            case 2:
                return new fragmentOrders();

            default: return new fragmentFoodList();
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
