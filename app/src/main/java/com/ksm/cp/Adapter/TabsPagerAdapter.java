package com.ksm.cp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ksm.cp.Activity.ItemBuying;
import com.ksm.cp.Activity.ItemSelling;
import com.ksm.cp.Activity.ItemWatching;

/**
 * Created by mparab on 4/10/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    Context context;
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabsPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context =context;
    }

    @Override
    public Fragment getItem(int index) {

       // String test = "manish" + "adad";
        switch (index) {
            case 0:
                return new ItemBuying();
              //return Fragment.instantiate(context, ItemBuying.class.getName());
                // Top Rated fragment activity
                //return new TopRatedFragment();
            case 1:
                return new ItemSelling();
                // Games fragment activity
                //return new GamesFragment();
            case 2:
                return new ItemWatching();
                // Movies fragment activity
                //return new MoviesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
