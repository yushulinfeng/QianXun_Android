package com.szdd.qianxun.sell.main.top;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.szdd.qianxun.tools.views.slidepage.ScrollAbleFragment;

import java.util.List;

//此类尽量不要修改
public class ListTopAdapter extends FragmentPagerAdapter {

    private List<ScrollAbleFragment> fragmentList;
    private List<String> titleList;

    public ListTopAdapter(FragmentManager fm,
                          List<ScrollAbleFragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
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
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
