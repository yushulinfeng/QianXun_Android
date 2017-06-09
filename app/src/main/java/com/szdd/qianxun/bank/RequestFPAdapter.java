package com.szdd.qianxun.bank;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/19 0019.
 */
public class RequestFPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentList;

    public RequestFPAdapter(FragmentManager fm, ArrayList<Fragment> list) {
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