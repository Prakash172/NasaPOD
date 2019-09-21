package com.androidcodeshop.obviousassigment.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.androidcodeshop.obviousassigment.activities.DetailViewActivity;
import com.androidcodeshop.obviousassigment.activities.MainActivity;
import com.androidcodeshop.obviousassigment.fragments.DetailViewFragment;

public class ImageViewPagerAdapter extends FragmentPagerAdapter {


    public ImageViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return DetailViewFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return MainActivity.responseDataModelArrayList.size();
    }


}
