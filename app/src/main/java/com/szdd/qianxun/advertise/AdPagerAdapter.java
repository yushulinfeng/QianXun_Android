package com.szdd.qianxun.advertise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by LLX on 2015/10/28.
 */
public class AdPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentList;

    public AdPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        fragmentList = list;
    }

    @Override
    public Fragment getItem(int pos) {
        return fragmentList.get(pos);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = ((Fragment) object);
        container.removeView(fragment.getView());
    }
}