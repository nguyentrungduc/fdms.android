package com.framgia.fdms.screen.device;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nhahv0902 on 6/13/2017.
 * <></>
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mTitles.add(title);
    }
}
