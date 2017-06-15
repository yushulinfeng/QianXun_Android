package com.szdd.qianxun.message.homepage;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;

import java.util.ArrayList;

/**
 * Created by linorz on 2016/3/26.
 */
public class HomePageAdapter extends FragmentPagerAdapter {
    ArrayList<ScrollAbleFragment> fragmentList;

    public HomePageAdapter(FragmentManager fm, ArrayList<ScrollAbleFragment> list) {
        super(fm);
        fragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        Fragment fragment = (Fragment) object;
        container.removeView(fragment.getView());
    }
}
