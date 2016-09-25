package com.ksm.cp.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;

import com.ksm.cp.Adapter.TabsPagerAdapter;
import com.ksm.cp.R;

/**
 * Created by mparab on 4/10/2016.
 */
public class BuySellProfile extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener {

    private com.ksm.cp.BaseClass.CustomViewPager viewPager;
    private FragmentPagerAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Buying", "Selling", "Watchlist" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_buy_sell_watch_profile);

        viewPager = (com.ksm.cp.BaseClass.CustomViewPager) findViewById(R.id.pager);
        viewPager.setPagingEnabled(false);
        actionBar =getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(),getApplicationContext());

        //below code is to hide the Action Title
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this)
                    );
        }

        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(1);
        actionBar.setSelectedNavigationItem(1);
    }


    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
       viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
