package com.efpro.bengkelmotor_01.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.efpro.bengkelmotor_01.Fragment.SliderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bagicode on 12/04/17.
 */

public class SliderPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "SliderPagerAdapter";

    List<Fragment> mFrags = new ArrayList<>();

    public SliderPagerAdapter(FragmentManager fm, List<Fragment> frags) {
        super(fm);
        mFrags = frags;
    }

    @Override
    public Fragment getItem(int position) {
        int index = position % mFrags.size();
        return SliderFragment.newInstance(mFrags.get(index).getArguments().getString("params"));
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}