package com.cdeducation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zl on 2014/7/21.
 */
public  class MainPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments;
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments=new ArrayList<Fragment>();
    }
    public  void setData(Fragment fragment){
        if(fragment!=null){
            fragments.add(fragment);
        }
        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        return fragments!=null&&fragments.size()>0?fragments.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
